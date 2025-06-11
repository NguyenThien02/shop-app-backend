package com.CIC.shop_app_backend.consumer;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.responses.OrderDetailResponse;
import com.CIC.shop_app_backend.services.IOrderDetailService;
import com.CIC.shop_app_backend.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final IOrderDetailService orderDetailService;
    private final ProductRepository productRepository;
    private final IProductService productService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumer(OrderDetailDTO orderDetailDTO) {
        try {
            if (orderDetailDTO.getOrderId() == null) {
                throw new IllegalArgumentException("OrderId không được null");
            }
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOderDetail(orderDetail);
            System.out.println("Tạo đơn hàng thành công: " + orderDetailResponse);

            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID: " + orderDetailDTO.getProductId()));
            double totalMoney = orderDetailDTO.getTotalMoney();
            double productPrice = product.getPrice();

            productService.updateStockQuantityProduct(orderDetailDTO.getProductId(),orderDetailDTO.getNumberOfProducts());
            System.out.println("Cập nhật số lượng sản phẩm trong kho thành công");

        } catch (Exception e) {
            System.err.println("Consumer xử lý lỗi: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Lỗi consumer, gửi sang DLQ", e);
        }
    }
}

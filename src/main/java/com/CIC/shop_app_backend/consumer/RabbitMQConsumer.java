package com.CIC.shop_app_backend.consumer;

import com.CIC.shop_app_backend.components.LoggerUtils;
import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.dtos.OrderRequestDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.responses.OrderDetailResponse;
import com.CIC.shop_app_backend.services.IInventoryService;
import com.CIC.shop_app_backend.services.IOrderDetailService;
import com.CIC.shop_app_backend.services.IOrderService;
import com.CIC.shop_app_backend.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final ObjectMapper objectMapper;
    private final IOrderDetailService orderDetailService;
    private final IProductService productService;
    private final IOrderService orderService;
    private final IInventoryService inventoryService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumer(String jsonString) {
        try {
            LoggerUtils.logInfo("Đang tiến hành xử lý đặt hàng");
            OrderRequestDTO orderRequestDTO = objectMapper.readValue(jsonString, OrderRequestDTO.class);
            OrderDTO orderDTO = orderRequestDTO.getOrderData();
            String token = orderRequestDTO.getAuthToken();

            Long orderId = orderService.createOrder(orderDTO, token).getOrderId();
            LoggerUtils.logInfo("Tạo đơn hàng thành công");

            for(OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()){
                orderDetailService.createOrderDetail(orderDetailDTO, orderId);
                LoggerUtils.logInfo("Tạo đơn hàng chi tiết thành công " + orderDetailDTO.getProductId());

                productService.updateStockQuantityProduct(orderDetailDTO.getProductId(), orderDetailDTO.getNumberOfProducts());
                LoggerUtils.logInfo("Cập nhật số lượng sản phẩm thành công" + orderDetailDTO.getProductId());

                System.out.println("Hoàn thành quá trình đặt hàng");
            }
        } catch (Exception e) {
            System.err.println("Consumer xử lý lỗi: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Lỗi consumer, gửi sang DLQ", e);
        }
    }
}

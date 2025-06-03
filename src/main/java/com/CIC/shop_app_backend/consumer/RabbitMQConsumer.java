package com.CIC.shop_app_backend.consumer;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.OrderDetailResponse;
import com.CIC.shop_app_backend.responses.OrderResponse;
import com.CIC.shop_app_backend.services.Impl.OrderDetailService;
import com.CIC.shop_app_backend.services.Impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final OrderDetailService orderDetailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consumer(OrderDetailDTO orderDetailDTO) {
        try {
            // Gây lỗi giả để test DLQ:
            if (orderDetailDTO.getOrderId() == null) {
                throw new IllegalArgumentException("OrderId không được null");
            }

            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOderDetail(orderDetail);
            System.out.println("Tạo đơn hàng thành công: " + orderDetailResponse);
        } catch (Exception e) {
            System.err.println("Consumer xử lý lỗi: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Lỗi consumer, gửi sang DLQ", e);
        }
    }
}

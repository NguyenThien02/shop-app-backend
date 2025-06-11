package com.CIC.shop_app_backend.consumer;

import com.CIC.shop_app_backend.components.LoggerUtils;
import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DLQConsumer {

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void handleDeadLetter(OrderDetailDTO orderDetailDTO) {
        LoggerUtils.logInfo("Nhận từ DLQ: " + orderDetailDTO);
        System.err.println("Nhận từ DLQ: " + orderDetailDTO);
        // Có thể lưu log, ghi DB, gửi email, retry sau, v.v...
    }
}

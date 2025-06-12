package com.CIC.shop_app_backend.consumer;

import com.CIC.shop_app_backend.components.LoggerUtils;
import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.MessageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DLQConsumer {

    private final RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void handleDeadLetter(Message failedMessage) {
        MessageProperties props = failedMessage.getMessageProperties();
        Integer retries = (Integer) props.getHeaders().getOrDefault("x-retries", 0);

        if (retries < 3) {
            props.getHeaders().put("x-retries", retries + 1);
            rabbitTemplate.send(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY ,failedMessage);
        }
        else {
            LoggerUtils.logInfo("Nhận từ DLQ (JSON String): " + failedMessage);
            System.err.println("Nhận từ DLQ (JSON String): " + failedMessage);
        }
    }
}

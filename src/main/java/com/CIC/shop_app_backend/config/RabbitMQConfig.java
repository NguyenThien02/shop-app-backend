package com.CIC.shop_app_backend.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "rabbit_mq_queue";
    public static final  String EXCHANGE = "rabbit_mq_exchange";
    public static final String  ROUTING_KEY = "rabbit_mq_r_key";

    // DLQ tên riêng
    public static final String DLQ = "rabbit_mq_dlq";
    public static final String DLX = "rabbit_mq_dlx";
    public static final String DLQ_ROUTING_KEY = "rabbit_mq_dlq_r_key";

    // DLQ Queue
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    // DLX Exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX);
    }

    // Binding DLQ với DLX
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DLQ_ROUTING_KEY);
    }

    // Chính queue xử lý, nhưng thêm DLX mapping
    @Bean
    public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX);
        args.put("x-dead-letter-routing-key", DLQ_ROUTING_KEY);
        args.put("x-expires", 60000); // 1 phút = 60.000 ms

//        QUEUE	Tên queue chính (thường là constant hoặc chuỗi tên queue)
//        true	durable: Queue sẽ tồn tại khi RabbitMQ khởi động lại (persistent)
//        false	exclusive: Queue không giới hạn chỉ một kết nối sử dụng
//        false	autoDelete: Queue sẽ không tự xóa khi không còn consumer
//        args	Các tham số cấu hình bổ sung, ở đây là DLX và DLQ routing key
        return new Queue(QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding mainBinding(
            @Qualifier("mainQueue") Queue queue,
            @Qualifier("mainExchange") DirectExchange exchange
    ) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }


//    //RabbitMQ sử dụng JSON cho message.
//    @Bean
//    public MessageConverter messageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }
//
//    // gửi/nhận message RabbitMQ với JSON converter.
//    @Bean
//    public AmqpTemplate getTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }

}
package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.OrderDetailResponse;
import com.CIC.shop_app_backend.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @Autowired
    // Tiêm phụ thuộc RabbitTemplate, công cụ gửi/nhận message qua RabbitMQ.
    private RabbitTemplate rabbitTemplate;


    @PostMapping("create")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ){
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,orderDetailDTO);
            return ResponseEntity.ok(new MessageResponse("Đang xử lý đơn hàng",true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

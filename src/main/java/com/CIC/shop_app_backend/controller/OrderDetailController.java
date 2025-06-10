package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.dtos.PaginationRequest;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.responses.ListOrderDetailResponse;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.OrderDetailResponse;
import com.CIC.shop_app_backend.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostMapping("create")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ){
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,RabbitMQConfig.ROUTING_KEY,orderDetailDTO);
            return ResponseEntity.ok(new MessageResponse("Đang xử lý đơn hàng ",true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{order-id}")
    public ResponseEntity<?> getOrderDetailByOrderId(
            @PathVariable("order-id") Long orderId,
            @ModelAttribute PaginationRequest paginationRequest
            ){
        try {
            PageRequest pageRequest = PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getLimit(),
                    Sort.by("orderDetailId").ascending()
            );
            Page<OrderDetail> orderDetailPage = orderDetailService.getOrderDetailByOrderId(orderId, pageRequest);
            Page<OrderDetailResponse> orderDetailResponsePage = orderDetailPage.map(orderDetail -> OrderDetailResponse.fromOderDetail(orderDetail));
            List<OrderDetailResponse> orderDetailResponses = orderDetailResponsePage.toList();
            return ResponseEntity.ok(ListOrderDetailResponse.builder()
                            .orderDetailList(orderDetailResponses)
                            .totalPages(orderDetailPage.getTotalPages())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

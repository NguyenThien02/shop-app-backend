package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.responses.OrderResponse;
import com.CIC.shop_app_backend.services.Impl.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("create")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO
    ){
        try {
            Order order = orderService.createOrder(orderDTO);
            OrderResponse orderResponse = OrderResponse.fromOrder(order);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

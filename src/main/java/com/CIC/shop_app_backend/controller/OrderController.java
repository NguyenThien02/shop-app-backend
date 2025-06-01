package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.dtos.PaginationRequest;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.responses.ListOrderResponse;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.OrderResponse;
import com.CIC.shop_app_backend.services.IOrderService;
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
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<?> placeOrder(
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

    @GetMapping("{user-id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable("user-id") Long userId,
                                              @ModelAttribute PaginationRequest paginationRequest
                                              ){
        try {
            PageRequest pageRequest = PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getLimit(),
                    Sort.by("orderId").ascending()
            );
            Page<Order> orderPage = orderService.getOrderByUserId(userId, pageRequest);
            Page<OrderResponse> orderResponsePage = orderPage.map(order -> OrderResponse.fromOrder(order));
            List<OrderResponse> orderResponses = orderResponsePage.toList();
            return ResponseEntity.ok(ListOrderResponse.builder()
                            .orderResponses(orderResponses)
                            .totalPages(orderPage.getTotalPages())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

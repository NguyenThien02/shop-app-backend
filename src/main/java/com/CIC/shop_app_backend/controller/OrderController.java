package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.config.RabbitMQConfig;
import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.dtos.OrderRequestDTO;
import com.CIC.shop_app_backend.dtos.PaginationRequest;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.responses.ListOrderResponse;
import com.CIC.shop_app_backend.responses.MessageResponse;
import com.CIC.shop_app_backend.responses.OrderResponse;
import com.CIC.shop_app_backend.services.IInventoryService;
import com.CIC.shop_app_backend.services.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final IInventoryService inventoryService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            @RequestHeader("Authorization") String token
    ) {
        try {
            for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
                if (!inventoryService.checkInventory(detailDTO.getProductId(), detailDTO.getNumberOfProducts())) {
                    Product product = productRepository.findById(detailDTO.getProductId())
                            .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID" + detailDTO.getProductId()));
                    return ResponseEntity.badRequest().body(
                            String.format("Không đủ hàng trong kho cho sản phẩm %s, số sản phẩm trong kho %d",
                                    product.getProductName(),
                                    product.getStockQuantity()
                            )
                    );
                }
            }
            String extractedToken = token.substring(7);
            orderDTO.setSellerId(null);
            OrderRequestDTO orderRequestDTO = new OrderRequestDTO(orderDTO, extractedToken);
            String jsonString = objectMapper.writeValueAsString(orderRequestDTO);

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, jsonString);
            return ResponseEntity.ok(new MessageResponse("Đang tiến hành xử lý Order", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("by-user-id/{user-id}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable("user-id") Long userId,
                                              @ModelAttribute PaginationRequest paginationRequest
    ) {
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

    @GetMapping("by-seller-id/{seller-id}")
    public ResponseEntity<?> getOrderBySellerId(@PathVariable("seller-id") Long sellerId,
                                                @ModelAttribute PaginationRequest paginationRequest) {
        try {
            PageRequest pageRequest = PageRequest.of(
                    paginationRequest.getPage(),
                    paginationRequest.getLimit(),
                    Sort.by("orderId").ascending()
            );
            Page<Order> orderPage = orderService.getOrderBySellerId(sellerId, pageRequest);
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

    @GetMapping("by-order-id/{order-id}")
    public ResponseEntity<?> getOrderById(
            @PathVariable("order-id") Long orderId
    ) {
        try {
            Order order = orderService.getOrderById(orderId);
            OrderResponse orderResponse = OrderResponse.fromOrder(order);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-order-status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam Long orderId,
                                               @RequestParam OrderStatus orderStatus) {
        try {
            Order order = orderService.updateOrderStatus(orderId, orderStatus);
            OrderResponse orderResponse = OrderResponse.fromOrder(order);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

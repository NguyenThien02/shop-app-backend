package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.OrderDetail;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.OrderDetailRepository;
import com.CIC.shop_app_backend.repository.OrderRepository;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO, Long orderID) {
        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new DataNotFoundException("Not found Order with ID: " + orderID));

        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found product with ID: " + orderDetailDTO.getProductId()));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());

        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public Page<OrderDetail> getOrderDetailByOrderId(Long orderId, PageRequest pageRequest) {
        return orderDetailRepository.findByOrder_OrderId(orderId, pageRequest);
    }
}

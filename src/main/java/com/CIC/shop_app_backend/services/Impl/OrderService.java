package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.OrderRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.services.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có id: " + orderDTO.getUserId()));

        User seller = userRepository.findById(orderDTO.getSellerId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người bán có id" + orderDTO.getSellerId()));

        Order orderNew = new Order();
        orderNew.setUser(user);
        orderNew.setSeller(seller);
        orderNew.setTotalAmount(orderDTO.getTotalAmount());
        orderNew.setShippingAddress(orderDTO.getShippingAddress());
        orderNew.setNotes(orderDTO.getNotes());

        return orderRepository.save(orderNew);
    }

    @Override
    public Page<Order> getOrderByUserId(Long userId, PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findByUser_UserId(userId, pageRequest);
        return orderPage;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy order với ID: " + orderId));
        return order;
    }
}

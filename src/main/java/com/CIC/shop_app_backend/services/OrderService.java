package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.OrderRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.services.Impl.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Not found user with id: " + orderDTO.getUserId()));

        Order orderNew = new Order();
        orderNew.setUser(user);
        orderNew.setTotalAmount(orderDTO.getTotalAmount());
        orderNew.setShippingAddress(orderDTO.getShippingAddress());
        orderNew.setNotes(orderDTO.getNotes());

        return orderRepository.save(orderNew);
    }
}

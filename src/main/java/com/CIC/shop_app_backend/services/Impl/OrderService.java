package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.components.JwtTokenUtils;
import com.CIC.shop_app_backend.dtos.OrderDTO;
import com.CIC.shop_app_backend.dtos.OrderDetailDTO;
import com.CIC.shop_app_backend.entity.Order;
import com.CIC.shop_app_backend.entity.Product;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.entity.Voucher;
import com.CIC.shop_app_backend.entity.enums.OrderStatus;
import com.CIC.shop_app_backend.entity.enums.VoucherType;
import com.CIC.shop_app_backend.exceptions.DataInvalidParamException;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.exceptions.VoucherException;
import com.CIC.shop_app_backend.repository.OrderRepository;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.repository.VoucherRepository;
import com.CIC.shop_app_backend.services.IOrderService;
import com.CIC.shop_app_backend.services.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final IVoucherService voucherService;

    @Override
    public Order createOrder(OrderDTO orderDTO, String extractedToken) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có id: " + orderDTO.getUserId()));

        Long userIdIntoken = jwtTokenUtils.extractUserId(extractedToken);

        if (!Objects.equals(userIdIntoken, user.getUserId())) {
            throw new DataInvalidParamException("Người dùng không có quyền tạo đơn hàng cho người khác");
        }

        User seller = userRepository.findById(orderDTO.getSellerId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người bán có id" + orderDTO.getSellerId()));

        Voucher voucher = new Voucher();

        Double totalAmount = 0D;
        for (OrderDetailDTO orderDetailDTO : orderDTO.getOrderDetails()) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có ID: " + orderDetailDTO.getProductId()));
            totalAmount += product.getPrice() * orderDetailDTO.getNumberOfProducts();
        }

        if(orderDTO.getVoucherId() != null) {
            voucher = voucherRepository.findById(orderDTO.getVoucherId())
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy voucher có ID" + orderDTO.getVoucherId()));
            if(voucher.getMinOrderCost() > totalAmount){
                throw new VoucherException("Tổng tiền đơn hàng không đủ để áp dụng mã giảm giá. Yêu cầu tối thiểu: " + voucher.getMinOrderCost());
            }
            if (voucher.getExpiryDatetime() != null && LocalDate.now().isAfter(voucher.getExpiryDatetime())) {
                throw new VoucherException("Mã giảm giá đã hết hạn sử dụng");
            }
            if(voucher.getLimitUsage() <= 0){
                throw new VoucherException("Số lượng mã giảm giá đã hết");
            }

            if(voucher.getType() == VoucherType.FIXED){
                totalAmount = totalAmount - voucher.getAmount();
            }else if(voucher.getType() == VoucherType.PERCENTAGE){
                totalAmount = totalAmount - (totalAmount * (voucher.getAmount() / 100.0));
            }

            voucher = voucherService.updateLimitUsage(voucher.getVoucherId(), voucher.getLimitUsage()-1);
        }
        else{
            voucher = null;
        }

        Order orderNew = new Order();
        orderNew.setUser(user);
        orderNew.setSeller(seller);
        orderNew.setShippingAddress(orderDTO.getShippingAddress());
        orderNew.setVoucher(voucher);
        orderNew.setTotalAmount(totalAmount);
        orderNew.setNotes(orderDTO.getNotes());
        return orderRepository.save(orderNew);
    }

    @Override
    public Page<Order> getOrderByUserId(Long userId, PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findByUser_UserId(userId, pageRequest);
        return orderPage;
    }

    @Override
    public Page<Order> getOrderBySellerId(Long sellerId, PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findBySeller_UserId(sellerId, pageRequest);
        return orderPage;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy order với ID: " + orderId));
        return order;
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy order với ID: " + orderId));
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }
}

package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.VoucherDTO;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.entity.Voucher;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.ProductRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.repository.VoucherRepository;
import com.CIC.shop_app_backend.services.IVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService {
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Voucher createVoucher(VoucherDTO voucherDTO) {
        User seller = userRepository.findById(voucherDTO.getSellerId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người bán có ID: " + voucherDTO.getSellerId()));
        Voucher voucher = new Voucher();
        voucher.setSeller(seller);
        voucher.setType(voucherDTO.getType());
        voucher.setAmount(voucherDTO.getAmount());
        voucher.setDescription(voucherDTO.getDescription());
        voucher.setMinOrderCost(voucherDTO.getMinOrderCost());
        voucher.setExpiryDatetime(voucherDTO.getExpiryDatetime());
        voucher.setLimitUsage(voucherDTO.getLimitUsage());

        return voucherRepository.save(voucher);
    }

    @Override
    public List<Voucher> getVoucherBySeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người bán có ID: "+ sellerId));

        List<Voucher> voucherList = voucherRepository.findBySeller_UserId(sellerId);
        return voucherList;
    }

    @Override
    public Voucher updateLimitUsage(Long voucherId, Integer limitUsage) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã giảm giá có ID: " + voucherId));

        voucher.setLimitUsage(limitUsage);
        return voucherRepository.save(voucher);
    }
}

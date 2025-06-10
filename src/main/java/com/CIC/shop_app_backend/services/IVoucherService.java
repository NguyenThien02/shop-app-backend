package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.VoucherDTO;
import com.CIC.shop_app_backend.entity.Voucher;

import java.util.List;

public interface IVoucherService {
    Voucher createVoucher(VoucherDTO voucherDTO);

    List<Voucher> getVoucherBySeller(Long sellerId);
}

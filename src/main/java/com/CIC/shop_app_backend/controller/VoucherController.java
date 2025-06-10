package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.VoucherDTO;
import com.CIC.shop_app_backend.entity.Voucher;
import com.CIC.shop_app_backend.services.Impl.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDTO voucherDTO) {
        try {
            Voucher created = voucherService.createVoucher(voucherDTO);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{seller-id}")
    public ResponseEntity<?> getVoucherBySeller(@PathVariable("seller-id") Long sellerId){
        try {
            List<Voucher> voucherList = voucherService.getVoucherBySeller(sellerId);
            return ResponseEntity.ok(voucherList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

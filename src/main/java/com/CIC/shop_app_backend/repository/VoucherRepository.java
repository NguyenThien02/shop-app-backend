package com.CIC.shop_app_backend.repository;

import com.CIC.shop_app_backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    List<Voucher> findBySeller_UserId(Long sellerId);
}

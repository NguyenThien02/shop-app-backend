package com.CIC.shop_app_backend.dtos;

import com.CIC.shop_app_backend.entity.enums.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class VoucherDTO {
    private Long sellerId;
    private VoucherType type;
    private Double amount;
    private String description;
    private Double minOrderCost;
    private LocalDate expiryDatetime;
    private Integer limitUsage;
}

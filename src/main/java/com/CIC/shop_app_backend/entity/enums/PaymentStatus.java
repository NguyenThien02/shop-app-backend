package com.CIC.shop_app_backend.entity.enums;

public enum PaymentStatus {
    UNPAID("UNPAID"),
    PAID("PAID"),
    REFUNDED("REFUNDED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
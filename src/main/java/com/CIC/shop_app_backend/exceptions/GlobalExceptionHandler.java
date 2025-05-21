package com.CIC.shop_app_backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> DataIntegrityViolationException(Exception e) {
        return ResponseEntity.status(432).body(e.getMessage());
    }
}

package com.CIC.shop_app_backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist // Kích hoạt khi: Entity được lưu lần đầu vào database khi gọi repository.save(entity)
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Kích hoạt khi: Entity được cập nhật (khi gọi repository.save(entity) trên một bản ghi đã tồn tại).
    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
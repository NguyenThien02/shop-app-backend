package com.CIC.shop_app_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CartDTO {
    @JsonProperty("user_id")
    private Long userId;
}

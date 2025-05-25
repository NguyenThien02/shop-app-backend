package com.CIC.shop_app_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserRegisterDTO {
    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    @JsonProperty("password")
    @NotBlank(message = "Password cannot be black")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    @JsonProperty("birthday")
    private Date birthday;

    @JsonProperty("address")
    private String address;

    @JsonProperty("role_id")
    @NotNull(message = "Role Id is required")
    private long roleId;
}

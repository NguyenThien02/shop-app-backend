package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.User;

import java.util.List;

public interface IUserService {
    User RegisterUser(UserRegisterDTO userRegisterDTO);
}

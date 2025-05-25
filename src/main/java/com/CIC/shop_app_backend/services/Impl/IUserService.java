package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.dtos.UserLoginDTO;
import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.User;

public interface IUserService {
    User registerUser(UserRegisterDTO userRegisterDTO);

    String loginUser(UserLoginDTO userLoginDTO);

    User getUserDetailFromToken(String toke) throws Exception;

    User getUserByPhoneNumber(String phoneNumber);

    User getUserByUserId(Long userId);
}

package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.UserLoginDTO;
import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.User;

import java.util.List;

public interface IUserService {
    User registerUser(UserRegisterDTO userRegisterDTO);

    String loginUser(UserLoginDTO userLoginDTO);

    User getUserDetailFromToken(String toke) throws Exception;

    User getUserByPhoneNumber(String phoneNumber);
}

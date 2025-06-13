package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.components.LoggerUtils;
import com.CIC.shop_app_backend.dtos.UserLoginDTO;
import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.responses.LoginResponse;
import com.CIC.shop_app_backend.responses.UserResponse;
import com.CIC.shop_app_backend.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor

public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ){
        try{
            LoggerUtils.logInfo("Đang cố gắng đăng ký người dùng mới bằng số điện thoại: " + userRegisterDTO.getPhoneNumber());

            if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
                LoggerUtils.logWarn("Mật khẩu không khớp để đăng ký người dùng: " , userRegisterDTO.getPhoneNumber());
                return ResponseEntity.badRequest().body("Mật khẩu không khớp");
            }
            User user = userService.registerUser(userRegisterDTO);
            UserResponse userResponse = UserResponse.fromUser(user);
            LoggerUtils.logInfo("Người dùng đã đăng ký thành công: " + user.getPhoneNumber());
            return ResponseEntity.ok(userResponse);
        }
        catch (Exception e){
            LoggerUtils.logError("Lỗi đăng ký người dùng: " + userRegisterDTO.getPhoneNumber(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody UserLoginDTO userLoginDTO
            ){
        try {
            LoggerUtils.logInfo("Đang cố gắng đăng nhập người dùng bằng số điện thoại: " + userLoginDTO.getPhoneNumber());
            String token = userService.loginUser(userLoginDTO);
            User user = userService.getUserByPhoneNumber(userLoginDTO.getPhoneNumber());

            UserResponse userResponse = UserResponse.fromUser(user);
            LoginResponse loginResponse = new LoginResponse(userResponse, token);

            LoggerUtils.logInfo("Đăng nhập thành công người dùng bằng số điện thoại: " + userLoginDTO.getPhoneNumber());

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getUserDetailByToken(@RequestHeader("Authorization") String token){
        try{
            String extractedToken = token.substring(7);
            User user = userService.getUserDetailFromToken(extractedToken);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("detail/{user-id}")
    public ResponseEntity<?> getUserDetailById(@PathVariable("user-id") Long userId){
        try{
            User user = userService.getUserByUserId(userId);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

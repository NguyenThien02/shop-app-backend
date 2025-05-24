package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.dtos.UserLoginDTO;
import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.responses.LoginResponse;
import com.CIC.shop_app_backend.responses.UserResponse;
import com.CIC.shop_app_backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ){
        try{
            if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.registerUser(userRegisterDTO);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody UserLoginDTO userLoginDTO
            ){
        try {
            String token = userService.loginUser(userLoginDTO);
            User user = userService.getUserByPhoneNumber(userLoginDTO.getPhoneNumber());
            UserResponse userResponse = UserResponse.fromUser(user);
            LoginResponse loginResponse = new LoginResponse(userResponse, token);
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

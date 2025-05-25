package com.CIC.shop_app_backend.controller;

import com.CIC.shop_app_backend.entity.Role;
import com.CIC.shop_app_backend.services.Impl.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getRole(){
        try {
            List<Role> roleList = roleService.getRole();
            return ResponseEntity.ok(roleList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

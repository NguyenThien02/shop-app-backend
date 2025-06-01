package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.entity.Role;
import com.CIC.shop_app_backend.repository.RoleRepository;
import com.CIC.shop_app_backend.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getRole() {
        return roleRepository.findAll();
    }
}

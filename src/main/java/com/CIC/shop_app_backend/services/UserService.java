package com.CIC.shop_app_backend.services;

import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.Role;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataIntegrityViolationException;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.RoleRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User RegisterUser(UserRegisterDTO userRegisterDTO) {
        String phoneNumber = userRegisterDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role newRole = roleRepository.findById(userRegisterDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        User newUser = User.builder()
                .fullName(userRegisterDTO.getFullName())
                .phoneNumber((userRegisterDTO.getPhoneNumber()))
                .password(userRegisterDTO.getPassword())
                .birthday(userRegisterDTO.getBirthday())
                .address(userRegisterDTO.getAddress())
                .role(newRole)
                .build();
        String password = userRegisterDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }
}

package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.components.JwtTokenUtils;
import com.CIC.shop_app_backend.dtos.UserLoginDTO;
import com.CIC.shop_app_backend.dtos.UserRegisterDTO;
import com.CIC.shop_app_backend.entity.Role;
import com.CIC.shop_app_backend.entity.User;
import com.CIC.shop_app_backend.exceptions.DataIntegrityViolationException;
import com.CIC.shop_app_backend.exceptions.DataNotFoundException;
import com.CIC.shop_app_backend.repository.RoleRepository;
import com.CIC.shop_app_backend.repository.UserRepository;
import com.CIC.shop_app_backend.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor

public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public User registerUser(UserRegisterDTO userRegisterDTO) {
        String phoneNumber = userRegisterDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại");
        }
        Role newRole = roleRepository.findById(userRegisterDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy vai trò"));
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

    @Override
    public String loginUser(UserLoginDTO userLoginDTO) {
        if(!userRepository.existsByPhoneNumber(userLoginDTO.getPhoneNumber())){
            throw new DataNotFoundException("Invalid phone number");
        }
        Optional<User> optionalUser = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber());
        User existingUser = optionalUser.get();

        if(!passwordEncoder.matches(userLoginDTO.getPassword(),existingUser.getPassword())){
            throw new BadCredentialsException("Incorrect phone number or password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword(),existingUser.getAuthorities());

        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser);
    }

    @Override
    public User getUserDetailFromToken(String toke) throws Exception {
        if(jwtTokenUtils.isTokenExpired(toke)){
            throw new Exception("Token đã hết hạn");
        }
        String phoneNumber = jwtTokenUtils.extractPhoneNumber(toke);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        return user.get();
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("No user found with phone number: " + phoneNumber));
    }

    @Override
    public User getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("No user found with id" + userId));
        return user;
    }


}

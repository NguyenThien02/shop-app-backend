package com.CIC.shop_app_backend.services.Impl;

import com.CIC.shop_app_backend.components.JwtTokenUtils;
import com.CIC.shop_app_backend.components.LoggerUtils;
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

import java.time.LocalDate;
import java.time.Period;
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
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            LoggerUtils.logInfo("Người dùng nhập số điện thoại trống.");
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        }
        if (!phoneNumber.matches("^0[0-9]{9}$")) {
            LoggerUtils.logInfo("Số điện thoại không đúng định dạng: {}" + phoneNumber);
            throw new IllegalArgumentException("Số điện thoại không đúng định dạng.");
        }
        if (phoneNumber.length() > 10 && phoneNumber.length() < 11) {
            throw new IllegalArgumentException("Số điện thoại phải từ 10 đến 11 ký tự.");
        }
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            LoggerUtils.logInfo("Số điện thoại {} đã tồn tại trong hệ thống: " + phoneNumber);
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại trong hệ thống.");
        }

        String fullName = userRegisterDTO.getFullName();
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        if (fullName.length() > 20) {
            throw new IllegalArgumentException("Họ tên không được vượt quá 20 ký tự.");
        }
        if (!fullName.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            throw new IllegalArgumentException("Họ tên chỉ được chứa chữ cái và khoảng trắng.");
        }

        String password = userRegisterDTO.getPassword();
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống.");
        }
        if (password.length() < 6 || password.length() > 16) {
            throw new IllegalArgumentException("Mật khẩu phải từ 6 đến 16 ký tự.");
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,16}$")) {
            throw new IllegalArgumentException("Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt.");
        }

        if (userRegisterDTO.getAddress() == null || userRegisterDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống.");
        }
        if (userRegisterDTO.getBirthday() == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống.");
        }
//        LocalDate today = LocalDate.now();
//        int age = Period.between(userRegisterDTO.getBirthday(), today).getYears();
//        if (age < 18) {
//            throw new IllegalArgumentException("Người dùng phải đủ 18 tuổi.");
//        }

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
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return userRepository.save(newUser);
    }

    @Override
    public String loginUser(UserLoginDTO userLoginDTO) {
        if (!userRepository.existsByPhoneNumber(userLoginDTO.getPhoneNumber())) {
            throw new DataNotFoundException("Số điện thoại chưa được đăng ký tài khoản");
        }
        User existingUser = userRepository.findByPhoneNumber(userLoginDTO.getPhoneNumber());

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Incorrect phone number or password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword(), existingUser.getAuthorities());

        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser);
    }

    @Override
    public User getUserDetailFromToken(String toke) throws Exception {
        if (jwtTokenUtils.isTokenExpired(toke)) {
            throw new Exception("Token đã hết hạn");
        }
        String phoneNumber = jwtTokenUtils.extractPhoneNumber(toke);
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("No user found with id" + userId));
        return user;
    }


}

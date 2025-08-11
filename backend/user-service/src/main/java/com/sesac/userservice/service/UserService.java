package com.sesac.userservice.service;

import com.sesac.userservice.dto.LoginRequest;
import com.sesac.userservice.dto.LoginResponse;
import com.sesac.userservice.entity.User;
import com.sesac.userservice.repository.UserRepository;
import com.sesac.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found id: " + id));
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User byEmail = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword()))
            throw new RuntimeException("Invalid email or password");

        String token = jwtTokenProvider.generateToken(byEmail.getEmail(), byEmail.getId());


        return new LoginResponse(token, byEmail.getId(), byEmail.getEmail(), byEmail.getName());
    }


}

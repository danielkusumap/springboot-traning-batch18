package com.bootcamp18.training.service.impl;

import com.bootcamp18.training.dto.request.ReqLoginDto;
import com.bootcamp18.training.dto.response.ResLoginDto;
import com.bootcamp18.training.entity.UserEntity;
import com.bootcamp18.training.exception.DataNotFoundException;
import com.bootcamp18.training.exception.UnauthorizedException;
import com.bootcamp18.training.repository.UserRepository;
import com.bootcamp18.training.service.AuthService;
import com.bootcamp18.training.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResLoginDto loginUser(ReqLoginDto reqLoginDto) {
        UserEntity user = userRepository.findByEmail(reqLoginDto.getEmail())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (!user.getPassword().equals(reqLoginDto.getPassword())){
            throw new UnauthorizedException("Email atau password salah");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        return ResLoginDto.builder()
                .email(user.getEmail())
                .token(token)
                .build();
    }
}

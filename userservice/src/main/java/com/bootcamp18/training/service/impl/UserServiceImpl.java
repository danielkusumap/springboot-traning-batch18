package com.bootcamp18.training.service.impl;

import com.bootcamp18.training.dto.request.ReqCreateUserDto;
import com.bootcamp18.training.dto.response.ResCreateUserDto;
import com.bootcamp18.training.dto.response.ResDetailUserDto;
import com.bootcamp18.training.entity.UserEntity;
import com.bootcamp18.training.exception.BadRequestException;
import com.bootcamp18.training.exception.DataNotFoundException;
import com.bootcamp18.training.repository.UserRepository;
import com.bootcamp18.training.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResCreateUserDto createUser(ReqCreateUserDto reqCreateUserDto) {
        // cek apakah email sudah ada?
        // kalo udah ada -> bad request

        boolean userExists = userRepository.existsByEmail(reqCreateUserDto.getEmail());
        if (userExists) {
            throw new BadRequestException("Email tidak tersedia");
        }

        UserEntity user = new UserEntity();
        user.setEmail(reqCreateUserDto.getEmail());
        user.setPassword(reqCreateUserDto.getPassword());
        user.setFirstName(reqCreateUserDto.getFirstName());
        user.setLastName(reqCreateUserDto.getLastName());
        user.setPhoneNumber(reqCreateUserDto.getPhoneNumber());

        UserEntity savedUser = userRepository.save(user);

        ResCreateUserDto response = new ResCreateUserDto();
        response.setFullName(savedUser.getFirstName() + " " + user.getLastName());

        return response;
    }

    @Override
    public ResDetailUserDto getDetailUser(Long id) {
        Optional<UserEntity> userOpt =  userRepository.findById(id);
        if (userOpt.isEmpty()) throw new DataNotFoundException("User tidak ditemukan");


        UserEntity user = userOpt.get();

//        ResDetailUserDto response = new ResDetailUserDto();
//        response.setFullName(user.getFirstName() + " " + user.getLastName());
//        response.setPhoneNumber(user.getPhoneNumber());
//        return response;

        return ResDetailUserDto.builder()
                .fullName(user.getFirstName() + " " + user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();

    }
}

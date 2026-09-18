package com.bootcamp18.training.service;

import com.bootcamp18.training.dto.request.ReqCreateUserDto;
import com.bootcamp18.training.dto.response.ResCreateUserDto;
import com.bootcamp18.training.dto.response.ResDetailUserDto;

public interface UserService {
    ResCreateUserDto createUser(ReqCreateUserDto reqCreateUserDto);
    ResDetailUserDto getDetailUser(Long id);
}

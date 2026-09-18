package com.bootcamp18.training.service;

import com.bootcamp18.training.dto.request.ReqLoginDto;
import com.bootcamp18.training.dto.response.ResLoginDto;

public interface AuthService {
    ResLoginDto loginUser(ReqLoginDto reqLoginDto);
}

package com.bootcamp18.training.controller;

import com.bootcamp18.training.dto.request.ReqLoginDto;
import com.bootcamp18.training.dto.response.BaseResponse;
import com.bootcamp18.training.dto.response.ResLoginDto;
import com.bootcamp18.training.service.AuthService;
import com.bootcamp18.training.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<ResLoginDto>> loginUser(
            @RequestBody ReqLoginDto request
            ) {
        ResLoginDto resLogin = authService.loginUser(request);
        BaseResponse<ResLoginDto> response = new BaseResponse<>();
        response.setMessage("Success login user");
        response.setData(resLogin);
        return ResponseEntity.ok(response);
    }
}

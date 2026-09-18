package com.bootcamp18.training.controller;

import com.bootcamp18.training.dto.request.ReqCreateUserDto;
import com.bootcamp18.training.dto.response.BaseResponse;
import com.bootcamp18.training.dto.response.ResCreateUserDto;
import com.bootcamp18.training.dto.response.ResDetailUserDto;
import com.bootcamp18.training.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<ResCreateUserDto>> registerUser(
            @Valid @RequestBody ReqCreateUserDto reqCreateUserDto
            ){
        ResCreateUserDto resCreateUserDto = userService.createUser(reqCreateUserDto);
        BaseResponse response = new BaseResponse();
        response.setData(resCreateUserDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResDetailUserDto>> getUserById (
            @PathVariable Long id
    ) {
        ResDetailUserDto respCreateUser = userService.getDetailUser(id);
//        BaseResponse<ResDetailUserDto> response = new BaseResponse<>();
//        response.setMessage("Success get user by id");
//        response.setData(respCreateUser);
        return ResponseEntity.ok(
                BaseResponse.<ResDetailUserDto>builder()
                        .data(respCreateUser)
                        .message("Success")
                        .build()
        );
    }

    @GetMapping("/param")
    public ResponseEntity<BaseResponse<ResDetailUserDto>> getUserByIdParam (
            @RequestParam Long id
    ) {
        ResDetailUserDto respCreateUser = userService.getDetailUser(id);
        BaseResponse<ResDetailUserDto> response = new BaseResponse<>();
        response.setMessage("Success get user by id");
        response.setData(respCreateUser);
        return ResponseEntity.ok(response);
    }
}

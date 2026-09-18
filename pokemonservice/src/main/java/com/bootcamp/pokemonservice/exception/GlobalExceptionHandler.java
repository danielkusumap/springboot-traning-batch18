package com.bootcamp.pokemonservice.exception;

import com.bootcamp.pokemonservice.dto.reponse.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequestException(BadRequestException exception) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setData(null);
        response.setMessage(exception.getMessage());
        response.setStatus("F");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleDataNotFoundException(DataNotFoundException exception) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setData(null);
        response.setStatus("F");
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}

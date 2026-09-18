package com.bootcamp18.training.exception;

import com.bootcamp18.training.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        ArrayList<String> errorMessages = new ArrayList<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errorMessages.add(error.getDefaultMessage()
                ));
        BaseResponse<Object> response = new BaseResponse<>();
        response.setData(errorMessages);
        response.setStatus("F");
        response.setMessage("Validation Error!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<Object>> handleUnauthorizedException(UnauthorizedException exception) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setData(null);
        response.setStatus("F");
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}

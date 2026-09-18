package com.bootcamp18.training.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) { super(message); }
}

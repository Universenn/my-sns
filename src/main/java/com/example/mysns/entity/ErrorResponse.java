package com.example.mysns.entity;

import com.example.mysns.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}

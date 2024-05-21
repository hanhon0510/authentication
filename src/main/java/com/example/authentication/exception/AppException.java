package com.example.authentication.exception;

import com.example.authentication.constant.ErrorCode;

public class AppException extends RuntimeException {
    private int statusCode;

    public AppException(String message) {
        super(message);
    }

    public AppException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}


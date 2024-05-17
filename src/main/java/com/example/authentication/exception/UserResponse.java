package com.example.authentication.exception;

public class UserResponse {
    private Integer statusCode;
    private String reasonPhrase;
    private String message;

    public UserResponse(){}


    public UserResponse(Integer statusCode,String reasonPhrase, String message) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.message = message;
    }
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

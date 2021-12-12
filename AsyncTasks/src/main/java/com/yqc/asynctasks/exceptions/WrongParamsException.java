package com.yqc.asynctasks.exceptions;

public class WrongParamsException extends RuntimeException {

    private String errorCode;

    private String message;

    public WrongParamsException() {
    }

    public WrongParamsException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public WrongParamsException(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WrongParamsException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
    
}

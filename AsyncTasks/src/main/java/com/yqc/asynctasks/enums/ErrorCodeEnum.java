package com.yqc.asynctasks.enums;

public enum ErrorCodeEnum {

    SuccessCode("W100.0", "success"),

    WrongParamsErrorCode("W100.1", "wrong parameters, please check!"),

    EntityNotFoundErrorCode("W100.2", "entity is not found, please check!"),

    TaskOverTimeErrorCode("W100.3", "task is overtime, please check!");

    private final String errorCode;

    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorCodeEnum{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

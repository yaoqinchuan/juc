package com.yqc.asynctasks.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WrongParamsException.class)
    @ResponseBody
    public ExceptionReturn wrongParameterExceptionHandler(WrongParamsException exception) {
        ExceptionReturn result = new ExceptionReturn("W401.01", exception.getMessage());
        return result;
    }
}

package com.yqc.asynctasks.exceptions;

import com.yqc.asynctasks.enums.TaskTech;
import com.yqc.asynctasks.tasks.exceptions.TaskFinishException;
import com.yqc.asynctasks.tasks.exceptions.TaskNeedToBeContinueException;
import com.yqc.asynctasks.utils.RedisTaskProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private RedisTaskProducer redisTaskProducer;

    @ExceptionHandler(WrongParamsException.class)
    @ResponseBody
    public ExceptionReturn wrongParameterExceptionHandler(WrongParamsException exception) {
        return new ExceptionReturn(exception.getErrorCode(), exception.getMessage());
    }


    @ExceptionHandler(TaskNeedToBeContinueException.class)
    @ResponseBody
    public void taskNeedToBeContinueExceptionHandler(TaskNeedToBeContinueException taskNeedToBeContinueException) {
        if (TaskTech.REDIS_TOPIC.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            redisTaskProducer.returnMessageToRedisTopic(taskNeedToBeContinueException.getTask(), taskNeedToBeContinueException.getParams());
        } else if (TaskTech.REDIS_SET.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            LOGGER.error("redis set not support now.");
        } else if (TaskTech.KAFKA.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            LOGGER.error("kafka not support now.");
        }
        LOGGER.error("task tech {} not support now.", taskNeedToBeContinueException.getParams().getTaskTech());
    }

    @ExceptionHandler(TaskFinishException.class)
    @ResponseBody
    public void taskFinishExceptionHandler(TaskFinishException taskNeedToBeContinueException) {
        if (TaskTech.REDIS_TOPIC.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            LOGGER.info("finish process is not implement.");
        } else if (TaskTech.REDIS_SET.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            LOGGER.error("redis set not support now.");
        } else if (TaskTech.KAFKA.equals(taskNeedToBeContinueException.getParams().getTaskTech())) {
            LOGGER.error("kafka not support now.");
        }
        LOGGER.error("task tech {} not support now.", taskNeedToBeContinueException.getParams().getTaskTech());
    }
}

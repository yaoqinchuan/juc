package com.yqc.asynctasks.utils;

import com.yqc.asynctasks.constants.Constants;
import com.yqc.asynctasks.entity.TaskHistoryDo;
import com.yqc.asynctasks.enums.ErrorCodeEnum;
import com.yqc.asynctasks.enums.TaskStatus;
import com.yqc.asynctasks.manager.TaskHistoryManager;
import com.yqc.asynctasks.tasks.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.function.Function;

@Component
@SuppressWarnings(value = "all")
public class RedisTaskConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTaskConsumer.class);

    @Autowired
    private RedissonUtils redissonUtils;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private TaskHistoryManager taskHistoryManager;

    private Boolean checkTaskValidation(AsyncTask asyncTask) {
        long startTime = asyncTask.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        TaskHistoryDo taskHistoryDo = asyncTask.getTaskHistoryDo();
        if (System.currentTimeMillis() > startTime + asyncTask.getTtl()) {
            LOGGER.info("task {} is expired." + asyncTask);
            taskHistoryDo.setStatus(TaskStatus.FAILED);
            taskHistoryDo.setErrorCode(ErrorCodeEnum.TaskOverTimeErrorCode.getErrorCode());
            taskHistoryDo.setErrorMessage(ErrorCodeEnum.TaskOverTimeErrorCode.getErrorMessage());
            taskHistoryManager.save(taskHistoryDo);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void consumerRedisTopic() {
        Function function = o -> {
            if (o instanceof AsyncTask) {
                AsyncTask task = (AsyncTask) o;
                if (!checkTaskValidation(task)) {
                    return null;
                }
                asyncTask.run(task.getParams());
            } else {
                LOGGER.error("task {} is not a AsyncRedisTask", o);
            }
            return null;
        };
        redissonUtils.consumerMessageFromTopic(Constants.ASYNC_TASK_REDIS_TOPIC_NAME, AsyncTask.class, function);
    }
}

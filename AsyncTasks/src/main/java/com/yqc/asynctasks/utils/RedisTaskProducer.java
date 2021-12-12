package com.yqc.asynctasks.utils;

import com.yqc.asynctasks.constants.Constants;
import com.yqc.asynctasks.dao.TaskHistoryDao;
import com.yqc.asynctasks.entity.TaskHistoryDo;
import com.yqc.asynctasks.enums.ErrorCodeEnum;
import com.yqc.asynctasks.enums.TaskStatus;
import com.yqc.asynctasks.exceptions.WrongParamsException;
import com.yqc.asynctasks.manager.TaskHistoryManager;
import com.yqc.asynctasks.params.AsyncParams;
import com.yqc.asynctasks.tasks.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@SuppressWarnings(value = "all")
public class RedisTaskProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisTaskProducer.class);

    @Autowired
    private RedissonUtils redissonUtils;

    @Autowired
    private TaskHistoryManager taskHistoryManager;

    private void initTaskByParams(AsyncParams params, AsyncTask asyncTask) {
        long ttl = Constants.REDIS_TASK_DEFAULT_EXPIRE_TIME;
        if (params.getTtl() > Constants.REDIS_TASK_MAX_EXPIRE_TIME) {
            ttl = Constants.REDIS_TASK_MAX_EXPIRE_TIME;
        } else if (params.getTtl() < Constants.REDIS_TASK_MIN_EXPIRE_TIME) {
            ttl = Constants.REDIS_TASK_MIN_EXPIRE_TIME;
        }
        asyncTask.setCreateTime(LocalDateTime.now());
        asyncTask.setGroupId(params.getGroupId());
        asyncTask.setTtl(ttl);
        asyncTask.setVersion(params.getVersion());
        asyncTask.setTaskId(UUID.randomUUID().toString().replace("-", ""));
        asyncTask.setParams(params);
        asyncTask.setStep(params.getStep());
        asyncTask.setUserId(params.getUserId());
    }

    public void initRedisTask(Class clazz, AsyncParams params) {

        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("InstantiationException " + e.getMessage());
            return;
        } catch (IllegalAccessException e) {
            LOGGER.error("IllegalAccessException " + e.getMessage());
            return;
        }
        if (!(instance instanceof AsyncTask)) {
            throw new WrongParamsException("task is invalid " + instance);
        }

        AsyncTask asyncTask = (AsyncTask) instance;
        initTaskByParams(params, asyncTask);
        TaskHistoryDo taskHistoryDo = new TaskHistoryDo();
        taskHistoryDo.setTaskId(asyncTask.getTaskId());
        taskHistoryDo.setStatus(TaskStatus.START);
        try {
            taskHistoryManager.save(taskHistoryDo);
        } catch (Exception exception) {
            LOGGER.error("save task {} failed.", taskHistoryDo);
            return;
        }

        redissonUtils.publishMessageToTopic(Constants.ASYNC_TASK_REDIS_TOPIC_NAME, asyncTask);
    }

    public void returnMessageToRedisTopic(AsyncTask asyncTask, AsyncParams params) {
        if (params.getStep() > 100 || params.getStep() < 0) {
            LOGGER.error("task {} step is larger than 100 or lower than 0, stop the task.", asyncTask);
            TaskHistoryDo taskHistoryDo = asyncTask.getTaskHistoryDo();
            taskHistoryDo.setStatus(TaskStatus.FAILED);
            taskHistoryDo.setErrorCode(ErrorCodeEnum.TaskOverTimeErrorCode.getErrorCode());
            taskHistoryDo.setErrorMessage(ErrorCodeEnum.TaskOverTimeErrorCode.getErrorMessage());
            taskHistoryManager.save(taskHistoryDo);
            return;
        }
        asyncTask.setStep(params.getStep());
        asyncTask.setParams(params);
        redissonUtils.publishMessageToTopic(Constants.ASYNC_TASK_REDIS_TOPIC_NAME, asyncTask);
    }
}

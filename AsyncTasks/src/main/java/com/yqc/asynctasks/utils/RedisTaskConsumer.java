package com.yqc.asynctasks.utils;

import com.yqc.asynctasks.constants.Constants;
import com.yqc.asynctasks.tasks.AsyncRedisTask;
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
    private AsyncRedisTask asyncRedisTask;

    private Boolean checkTaskValidation(AsyncRedisTask asyncRedisTask) {
        long startTime = asyncRedisTask.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if (System.currentTimeMillis() > startTime + asyncRedisTask.getTtl()) {
            LOGGER.info("task {} is expired." + asyncRedisTask);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void consumerRedisTopic() {
        Function function = o -> {
            if (o instanceof AsyncRedisTask) {
                AsyncRedisTask task = (AsyncRedisTask) o;
                if (!checkTaskValidation(task)) {
                    return null;
                }
                ;
                asyncRedisTask.run(task.getParams());
            } else {
                LOGGER.error("task {} is not a AsyncRedisTask", o);
            }
            return null;
        };
        redissonUtils.consumerMessageFromTopic(Constants.ASYNC_TASK_REDIS_TOPIC_NAME, AsyncRedisTask.class, function);
    }
}

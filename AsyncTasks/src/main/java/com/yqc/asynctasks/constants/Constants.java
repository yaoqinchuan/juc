package com.yqc.asynctasks.constants;

public class Constants {
    // redis task 使用topic名称
    public static final String ASYNC_TASK_REDIS_TOPIC_NAME = "async_task_redis_topic";

    // 默认群组
    public static final String DEFAULT_GROUP_ID = "0123456789";

    // redis task 的默认超时时间1小时
    public static final long REDIS_TASK_DEFAULT_EXPIRE_TIME = 60 * 60;

    // redis task 的最大超时时间12小时
    public static final long REDIS_TASK_MAX_EXPIRE_TIME = 12 * 60 * 60;

    // redis task 的最小超时时间10分钟
    public static final long REDIS_TASK_MIN_EXPIRE_TIME = 10 * 60;
}

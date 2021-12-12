package com.yqc.asynctasks.schedule;

import com.yqc.asynctasks.utils.RedisTaskConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Configuration
@EnableScheduling
public class ScheduleTask {

    private RedisTaskConsumer consumer;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleTask.class);

    @SuppressWarnings(value = "all")
    @Scheduled(cron = "*/10 * * * * ?")
    private void consumerRedisTopicMessage(){
        consumer.consumerRedisTopic();
    }
}

package com.yqc.asynctasks.manager;

import com.yqc.asynctasks.constants.Constants;
import com.yqc.asynctasks.enums.TaskTech;
import com.yqc.asynctasks.params.SimplePrintTaskParams;
import com.yqc.asynctasks.tasks.SimplePrintAsyncTask;
import com.yqc.asynctasks.utils.RedisTaskProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncManager {
    private final Logger LOGGER = LoggerFactory.getLogger(AsyncManager.class);

    @Autowired
    private RedisTaskProducer producer;

    public void createRedisTopicAsyncTask(String startMessage) {
        SimplePrintTaskParams simplePrintTaskParams = new SimplePrintTaskParams();
        simplePrintTaskParams.setTaskTech(TaskTech.REDIS_TOPIC);
        simplePrintTaskParams.setPrintMessage(startMessage);
        simplePrintTaskParams.setUserId("yqc1994");
        simplePrintTaskParams.setGroupId(Constants.DEFAULT_GROUP_ID);
        producer.initRedisTask(SimplePrintAsyncTask.class, simplePrintTaskParams);
    }
}

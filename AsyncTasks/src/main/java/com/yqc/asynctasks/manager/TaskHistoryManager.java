package com.yqc.asynctasks.manager;

import com.yqc.asynctasks.dao.TaskHistoryDao;
import com.yqc.asynctasks.entity.TaskHistoryDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskHistoryManager {

    private final Logger LOGGER = LoggerFactory.getLogger(TaskHistoryManager.class);

    @Autowired
    private TaskHistoryDao taskHistoryDao;

    public void save(TaskHistoryDo taskHistoryDo) {
        LOGGER.info("TaskHistoryDo {} saved.", taskHistoryDo);
        taskHistoryDao.save(taskHistoryDo);
    }

    public void delete(TaskHistoryDo taskHistoryDo) {
        LOGGER.info("TaskHistoryDo {} deleted.", taskHistoryDo);
        taskHistoryDao.delete(taskHistoryDo);
    }
}

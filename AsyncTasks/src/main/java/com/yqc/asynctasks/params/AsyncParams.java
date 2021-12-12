package com.yqc.asynctasks.params;

import com.yqc.asynctasks.enums.TaskTech;

import java.io.Serializable;

public class AsyncParams implements Serializable {

    private long ttl;

    private Integer version = 1;

    private String groupId;

    private String userId;

    private Integer step;

    // 使用哪种异步队列,默认使用topic
    private TaskTech taskTech = TaskTech.REDIS_TOPIC;

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public TaskTech getTaskTech() {
        return taskTech;
    }

    public void setTaskTech(TaskTech taskTech) {
        this.taskTech = taskTech;
    }

    @Override
    public String toString() {
        return "AsyncParams{" +
                "ttl=" + ttl +
                ", version=" + version +
                ", groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", step=" + step +
                ", taskTech=" + taskTech +
                '}';
    }
}

package com.yqc.asynctasks.tasks;

import com.yqc.asynctasks.params.AsyncRedisParams;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Component
public abstract class AsyncRedisTask<T extends AsyncRedisParams> implements Serializable {

    protected Integer step;

    protected Long ttl;

    protected Integer version;

    protected LocalDateTime createTime;

    protected String userId;

    protected String groupId;

    private T params;

    private String taskId;

    public abstract void run(T asyncRedisParams);

    public void doTask(T params) {
        this.run(params);
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "AsyncRedisTask{" +
                "step=" + step +
                ", ttl=" + ttl +
                ", version=" + version +
                ", createTime=" + createTime +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", params=" + params +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}

package com.yqc.asynctasks.params;

import java.io.Serializable;

public class AsyncRedisParams implements Serializable {

    private long ttl;

    private Integer version = 1;

    private String groupId;

    private String userId;

    private Integer step;

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

    @Override
    public String toString() {
        return "AsyncRedisParams{" +
                "ttl=" + ttl +
                ", version=" + version +
                ", groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", step=" + step +
                '}';
    }
}

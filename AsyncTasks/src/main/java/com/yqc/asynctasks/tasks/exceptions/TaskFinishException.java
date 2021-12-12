package com.yqc.asynctasks.tasks.exceptions;

import com.yqc.asynctasks.params.AsyncParams;
import com.yqc.asynctasks.tasks.AsyncTask;

@SuppressWarnings(value = "all")
public class TaskFinishException extends RuntimeException {

    private AsyncTask task;

    private AsyncParams params;

    public AsyncTask getTask() {
        return task;
    }

    public void setTask(AsyncTask task) {
        this.task = task;
    }

    public AsyncParams getParams() {
        return params;
    }

    public void setParams(AsyncParams params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "TaskFinishException{" +
                "task=" + task +
                ", params=" + params +
                '}';
    }
}

package com.yqc.asynctasks.tasks.exceptions;

import com.yqc.asynctasks.params.AsyncParams;
import com.yqc.asynctasks.tasks.AsyncTask;

@SuppressWarnings(value = "all")
public class TaskNeedToBeContinueException extends RuntimeException {

    private AsyncTask task;

    private AsyncParams params;

    public TaskNeedToBeContinueException(AsyncTask task, AsyncParams params) {
        this.task = task;
        this.params = params;
    }

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
        return "TaskNeedToBeContinueException{" +
                "task=" + task +
                ", params=" + params +
                '}';
    }
}

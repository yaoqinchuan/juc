package com.yqc.asynctasks.params;

public class SimplePrintTaskParams extends AsyncRedisParams {

    private String printMessage;

    public String getPrintMessage() {
        return printMessage;
    }

    public void setPrintMessage(String printMessage) {
        this.printMessage = printMessage;
    }

    @Override
    public String toString() {
        return "SimplePrintTaskParams{" +
                "printMessage='" + printMessage + '\'' +
                '}';
    }
}

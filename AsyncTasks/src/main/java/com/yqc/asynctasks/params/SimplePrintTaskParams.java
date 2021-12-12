package com.yqc.asynctasks.params;

public class SimplePrintTaskParams extends AsyncParams {

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


package com.event;

public class TaskParam {
    private String strMsg = "";
    private int iFinalPid = 0;

    public int pid() {
        return iFinalPid;
    }

    public String msg() {
        return strMsg;
    }

    private TaskParam() {
    }

    public static TaskParam wrap(int pid, String msg) {
        TaskParam param = new TaskParam();
        param.strMsg = msg;
        param.iFinalPid = pid;
        return param;
    }
}


package com.pace.event;

public class TaskInput {
    private String strMsg = "";
    private int iFinalPid = 0;

    public int pid() {
        return iFinalPid;
    }

    public String msg() {
        return strMsg;
    }

    private TaskInput() {
    }

    public static TaskInput wrap(int pid, String msg) {
        TaskInput param = new TaskInput();
        param.strMsg = msg;
        param.iFinalPid = pid;
        return param;
    }
}

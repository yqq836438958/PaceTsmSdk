
package com.pace.event;

public class TaskEventSource {
    private String strMsg = "";
    private int iTargetId = 0;

    public int targetId() {
        return iTargetId;
    }

    public String msg() {
        return strMsg;
    }

    private TaskEventSource() {
    }

    public static TaskEventSource wrap(int pid, String msg) {
        TaskEventSource param = new TaskEventSource();
        param.strMsg = msg;
        param.iTargetId = pid;
        return param;
    }
}

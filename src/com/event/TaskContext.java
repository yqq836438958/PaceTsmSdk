
package com.event;

public class TaskContext {
    private BaseTask baseTask;
    private TaskParam param;
    private int curPid = -1;

    TaskContext(TaskParam param) {
        this.param = param;
    }

    public TaskParam getParam() {
        return param;
    }

    BaseTask getBaseTask() {
        return baseTask;
    }

    public int getCurPid() {
        return curPid;
    }
}

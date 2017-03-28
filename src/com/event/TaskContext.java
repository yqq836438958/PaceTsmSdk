
package com.event;

public class TaskContext {
    private BaseTask baseTask;
    private TaskInput param;
    private ProcessIdRouter router;
    private int curPid = -1;

    TaskContext(TaskInput param) {
        this.param = param;
    }

    public ProcessIdRouter getRouter() {
        return router;
    }

    public TaskInput getParam() {
        return param;
    }

    BaseTask getBaseTask() {
        return baseTask;
    }

    public int getCurPid() {
        return curPid;
    }
}

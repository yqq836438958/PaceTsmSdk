
package com.pace.event;

public class TaskContext {
    private BaseTask baseTask;
    private TaskEventSource param;
    private PidRouter pidRouter;
    private int curPid = -1;

    TaskContext(TaskEventSource param, PidRouter router) {
        this.param = param;
        pidRouter = router;
    }

    public PidRouter getPidRouter() {
        return pidRouter;
    }

    public TaskEventSource getParam() {
        return param;
    }

    BaseTask getBaseTask() {
        return baseTask;
    }

    public int getCurPid() {
        return curPid;
    }
}

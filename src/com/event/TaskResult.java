
package com.event;

import com.event.TaskEngineImp.IResultHandler;

public class TaskResult {
    public static final int TASK_INIT = 0;
    public static final int TASK_FINISH = 1;
    public static final int TASK_SWITH = 2;
    private int iRet;
    private Object object;
    private IResultHandler handler = null;

    private TaskResult(int ret, Object object) {
        iRet = ret;
        this.object = object;
    }

    public int getRet() {
        return iRet;
    }

    public Object getOject() {
        return object;
    }

    public IResultHandler getResultHandler() {
        return handler;
    }

    public static TaskResult emptyResult() {
        return new TaskResult(TASK_INIT, null);
    }

    public static TaskResult newResult(Object object) {
        return new TaskResult(TASK_INIT, object);
    }

    public static TaskResult newResult(int ret, Object object) {
        return new TaskResult(ret, object);
    }
}

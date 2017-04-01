
package com.pace.event;

import com.pace.event.TaskEngineImp.IEventHandler;
import com.pace.event.TaskEngineImp.NextProcessHandler;
import com.pace.event.TaskEngineImp.EndProcessHandler;
import com.pace.event.TaskEngineImp.RepeatProcessHandler;
import com.pace.event.TaskEngineImp.DefaultProcessHandler;

public class TaskEvent {
    private Object object;
    private IEventHandler handler = null;

    private TaskEvent(Object object) {
        this(object, null);
    }

    private TaskEvent(Object object, IEventHandler handler) {
        this.object = object;
        this.handler = handler;
    }

    public Object getOject() {
        return object;
    }

    public IEventHandler getResultHandler() {
        return handler;
    }

    public static TaskEvent emptyResult() {
        return new TaskEvent(null);
    }

    public static TaskEvent repeat(Object object) {
        return new TaskEvent(object, new RepeatProcessHandler());
    }

    public static TaskEvent end(Object object) {
        return new TaskEvent(object, new EndProcessHandler());
    }

    public static TaskEvent next(Object object) {
        return new TaskEvent(object, new NextProcessHandler());
    }
}

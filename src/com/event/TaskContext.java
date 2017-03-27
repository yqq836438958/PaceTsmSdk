
package com.event;

import java.util.concurrent.LinkedBlockingQueue;

public class TaskContext {
    private LinkedBlockingQueue<ITask> queue;
    private BaseTask baseTask;

    TaskContext() {

    }

    BaseTask getBaseTask() {
        return baseTask;
    }

    LinkedBlockingQueue<ITask> getTaskList() {
        return queue;
    }

    boolean addTask(ITask task) {
        synchronized (TaskContext.this) {
            return queue.add(task);
        }
    }
}

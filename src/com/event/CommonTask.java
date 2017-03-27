
package com.event;

import com.event.IBaseProcess;
import com.event.ITask;

import bolts.Continuation;
import bolts.Task;

public class CommonTask implements ITask {
    private BaseTask mBaseTask = null;

    public CommonTask(TaskContext context) {
        mBaseTask = context.getBaseTask();
    }

    private IBaseProcess findProcess() {
        return null;
    }

    @Override
    public TaskResult exec() {
        handProcess();
        mBaseTask.waitForComplete();
        return (TaskResult) mBaseTask.getResult();
    }

    private void handProcess() {
        Continuation<TaskResult, TaskResult> processContinuation = new Continuation<TaskResult, TaskResult>() {

            @Override
            public TaskResult then(Task<TaskResult> task) throws Exception {
                IBaseProcess process = findProcess();
                if (process != null) {
                    return process.process(task.getResult());
                }
                return TaskResult.emptyResult();
            }
        };
        mBaseTask.append(processContinuation);
    }
}


package com.pace.event;

import com.pace.processor.ProcessorPools;

import bolts.Continuation;
import bolts.Task;

public class CommonTask implements ITask {
    private BaseTask mBaseTask = null;
    protected TaskContext mContext = null;
    private int mPid = 0;

    public CommonTask(TaskContext context) {
        mBaseTask = context.getBaseTask();
        mContext = context;
    }

    public CommonTask(TaskContext context, int pid) {
        mBaseTask = context.getBaseTask();
        mContext = context;
        mPid = pid;
    }

    private IBaseProcessor findProcess() {
        return ProcessorPools.get().getProcess(mPid);
    }

    @Override
    public TaskResult exec() {
        handProcess();
        return (TaskResult) mBaseTask.getResult();
    }

    private void handProcess() {
        Continuation<TaskResult, TaskResult> processContinuation = new Continuation<TaskResult, TaskResult>() {

            @Override
            public TaskResult then(Task<TaskResult> task) throws Exception {
                IBaseProcessor process = findProcess();
                if (process != null) {
                    return process.process(task.getResult());
                }
                return TaskResult.emptyResult();
            }
        };
        mBaseTask.append(processContinuation);
        mBaseTask.waitForComplete();
    }

}

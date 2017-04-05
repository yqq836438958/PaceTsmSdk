
package com.pace.event;

import com.pace.processor.ProcessorPools;

import bolts.Continuation;
import bolts.Task;

public class CommonTask implements ITask {
    private BaseTask mBaseTask = null;
    protected TaskContext mContext = null;
    private int nextTaskPid = -1;

    public CommonTask(TaskContext context) {
        this(context, -1);
    }

    public CommonTask(TaskContext context, int pid) {
        mBaseTask = context.getBaseTask();
        mContext = context;
        nextTaskPid = pid;
    }

    private IBaseProcessor findProcess() {
        if (nextTaskPid > 0) {
            return ProcessorPools.get().getProcess(mContext.getParam(), nextTaskPid);
        }
        return ProcessorPools.get().getProcess(mContext.getParam());
    }

    @Override
    public void clear() {
        ProcessorPools.get().clear();
    }

    @Override
    public TaskEvent exec() {
        handProcess();
        return (TaskEvent) mBaseTask.getResult();
    }

    private void handProcess() {
        Continuation<TaskEvent, TaskEvent> processContinuation = new Continuation<TaskEvent, TaskEvent>() {

            @Override
            public TaskEvent then(Task<TaskEvent> task) throws Exception {
                IBaseProcessor process = findProcess();
                if (process != null) {
                    return process.process(task.getResult());
                }
                return TaskEvent.error();
            }
        };
        mBaseTask.append(processContinuation);
        mBaseTask.waitForComplete();
    }

}

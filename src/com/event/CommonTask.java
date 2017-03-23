
package com.event;

import bolts.Continuation;
import bolts.Task;

public class CommonTask implements ITask {
    private BoltsTask mBoltsTask = null;
    private IBaseProcess mProcess = null;

    @Override
    public TaskResult exec() {
        if (mBoltsTask == null) {
            mBoltsTask = BoltsTask.create();
        }
        handProcess();
        mBoltsTask.waitForComplete();
        return (TaskResult) mBoltsTask.getResult();
    }

    private void handProcess() {
        Continuation<TaskResult, TaskResult> processContinuation = new Continuation<TaskResult, TaskResult>() {

            @Override
            public TaskResult then(Task<TaskResult> task) throws Exception {
                if (mProcess != null) {
                    return mProcess.process(task.getResult());
                }
                return TaskResult.emptyResult();
            }
        };
        mBoltsTask.append(processContinuation);
    }

    @Override
    public void setProcess(IBaseProcess process) {
        mProcess = process;
    }

}

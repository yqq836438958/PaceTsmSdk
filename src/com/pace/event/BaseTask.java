
package com.pace.event;

import bolts.Continuation;
import bolts.Task;

public class BaseTask {
    private Task mCurTask = null;

    private BaseTask() {
        mCurTask = Task.forResult(TaskResult.emptyResult());
    }

    static BaseTask create() {
        return new BaseTask();
    }

    final <TTaskResult, TContinuationResult> BaseTask append(
            Continuation<TTaskResult, TContinuationResult> continuation) {
        mCurTask = mCurTask.continueWith(continuation);
        return this;
    }

    Object getResult() {
        return mCurTask.getResult();
    }

    boolean isCompleted() {
        // mCurTask.getre
        return mCurTask.isCompleted();
    }

    void waitForComplete() {
        try {
            mCurTask.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void clear() {
        // mCurTask.wait
    }
}

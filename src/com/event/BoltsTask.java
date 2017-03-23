
package com.event;

import bolts.Continuation;
import bolts.Task;

public class BoltsTask {
    private Task mCurTask = null;

    private BoltsTask() {
        mCurTask = Task.forResult(TaskResult.emptyResult());
    }

    static BoltsTask create() {
        return new BoltsTask();
    }

    final <TTaskResult, TContinuationResult> BoltsTask append(
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

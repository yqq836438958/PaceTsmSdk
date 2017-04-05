
package com.pace.event;

import bolts.Continuation;
import bolts.Task;

public class BaseTask {
    private Task mCurTask = null;

    private BaseTask() {
        mCurTask = Task.forResult(TaskEvent.empty());
    }

    static BaseTask create() {
        return new BaseTask();
    }

    final <TTaskEvent, TContinuationResult> BaseTask append(
            Continuation<TTaskEvent, TContinuationResult> continuation) {
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

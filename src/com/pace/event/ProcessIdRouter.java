
package com.pace.event;

import java.util.ArrayList;
import java.util.List;

public class ProcessIdRouter {
    // TODO 应该由调用者去决定
    public static final int MODE_LOOP = 0;
    public static final int MODE_QUEUE = 1;
    public static final int MODE_LOOP_QUEUE = 2;
    public static final int MODE_QUEUE_LOOP = 3;
    private int mCurStep = -1;
    private int mCurLoopStep = -1;
    private int mCurQueueStep = -1;
    private int mCurMode = MODE_LOOP;
    private int count = -1;
    private int mode = MODE_LOOP;
    private List<Integer> mLoopPids = new ArrayList<Integer>();
    private List<Integer> mQueuePids = new ArrayList<Integer>();

    ProcessIdRouter(int mode, List<Integer> list) {
        mCurMode = mode;
    }

    ProcessIdRouter() {
        // TODO Auto-generated constructor stub
    }

    final int firstPid() {
        return 0;
    }

    final int repeatPid() {
        return mCurStep;
    }

    final int nextPid() {
        return 0;
    }

    final void clear() {
        mLoopPids.clear();
        mQueuePids.clear();
    }

    private int nextLoopPid() {
        int target = -1;
        int length = mLoopPids.size();
        for (int i = 0; i < length; i++) {
            if (mCurLoopStep == mLoopPids.get(i)) {
                target = i;
                break;
            }
        }
        if (target < 0) {
            return -1;
        }
        if (target >= length - 1) {
            return mLoopPids.get(0);
        }
        return mLoopPids.get(target + 1);
    }

    private int nextQueuePid() {
        return 0;
    }
}

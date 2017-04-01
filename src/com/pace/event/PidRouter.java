
package com.pace.event;

import java.util.List;

public class PidRouter {
    private List<Integer> mPidList;
    private int mIndex = 0;

    public PidRouter(List<Integer> list) {
        mPidList = list;
    }

    public int first() {
        mIndex = 0;
        return getCurPidVal();
    }

    public int repeat() {
        return getCurPidVal();
    }

    public int prev() {
        mIndex--;
        if (mIndex < 0) {
            mIndex = mPidList.size() - 1;
        }
        return getCurPidVal();
    }

    public int next() {
        mIndex++;
        if (mIndex >= mPidList.size()) {
            mIndex = 0;
        }
        return getCurPidVal();
    }

    private int getCurPidVal() {
        return mPidList.get(mIndex);
    }
}

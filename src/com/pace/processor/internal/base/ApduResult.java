
package com.pace.processor.internal.base;

public class ApduResult<TYPE> {
    private TYPE mResult;
    private ApduStep mNewStep = null;

    public ApduResult(ApduStep step, TYPE obj) {
        mNewStep = step;
        mResult = obj;
    }

    public TYPE get() {
        return mResult;
    }

    public boolean call(ApduStep oldStep) {
        if (mNewStep != null) {
            mNewStep.setParam(mResult);
            return oldStep.switchStep(mNewStep);
        }
        return false;
    }
}

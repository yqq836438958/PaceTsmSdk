
package com.pace.processor.internal.base;

public class ApduResult<TYPE> {
    private TYPE mResult;
    private APDU_STEP mNewStep = null;

    public ApduResult(APDU_STEP step, TYPE obj) {
        mNewStep = step;
        mResult = obj;
    }

    public TYPE get() {
        return mResult;
    }

    public void call(ApduStep oldStep) {
        if (mNewStep != null) {
            ApduStep newStep = mMap.get(mNewStep);
            if (newStep != null) {
                newStep.setParam(mResult);
            }
            oldStep.switchStep(newStep);
        }
    }

}


package com.pace.processor.internal.base;

import com.pace.step.Step;

public abstract class ApduStep extends Step<APDU_STEP> {

    private Object mInputParam = null;

    public ApduStep(APDU_STEP step) {
        super(step);
    }

    public void setParam(Object obj) {
        mInputParam = obj;
    }

    protected Object getParam() {
        return mInputParam;
    }
}

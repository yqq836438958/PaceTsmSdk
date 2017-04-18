
package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.APDU_STEP;
import com.pace.processor.internal.base.ApduChainController.ApduChainNode;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.ApduStep;
import com.pace.processor.internal.base.IApduProvider;

import java.util.HashMap;
import java.util.List;

public abstract class CardBaseBusiness extends ApduChainNode {

    private HashMap<APDU_STEP, ApduStep> mMap = new HashMap<APDU_STEP, ApduStep>();
    protected IApduProvider mApduProvider = null;
    private ApduStep mCurStep = null;
    private ApduStep mFinalStep = new ApduStep(APDU_STEP.FINAL) {

        @Override
        public void onStepHandle() {
            // do nothing??
        }
    };
    private ApduStep mTransmitStep = new ApduStep(APDU_STEP.APDU_TRANSIMT) {

        @Override
        public void onStepHandle() {
            // TODO Auto-generated method stub

        }

    };
    protected ApduStep mProvideStep = new ApduStep(APDU_STEP.APDU_PROVIDE) {

        @Override
        public void onStepHandle() {
            Object input = getParam();
            ApduResult<APDU> result = onApduProvide(input);
            result.call(this);
        }

    };
    private ApduStep mPrepareStep = new ApduStep(APDU_STEP.PREPARE) {

        @Override
        public void onStepHandle() {
            // TODO Auto-generated method stub

        }
    };
    private ApduStep mConsumeStep = new ApduStep(APDU_STEP.APDU_CONSUME) {

        @Override
        public void onStepHandle() {
            // TODO Auto-generated method stub

        }

    };

    public CardBaseBusiness() {
        mMap.put(APDU_STEP.PREPARE, mPrepareStep);
        mMap.put(APDU_STEP.APDU_PROVIDE, mProvideStep);
        mMap.put(APDU_STEP.APDU_TRANSIMT, mTransmitStep);
        mMap.put(APDU_STEP.APDU_CONSUME, mConsumeStep);
        mMap.put(APDU_STEP.FINAL, mFinalStep);
    }

    @Override
    public RET onCall(String input) {
        mPrepareStep.onStepEnter();
        // TODO ...
        if (mFinalStep.isCurrentStep()) {
        }
        return finalResult();
    }

    protected abstract ApduResult<Boolean> onCachPrepare();

    protected abstract ApduResult<APDU> onApduProvide(Object input);

    protected abstract ApduResult<APDU> onApduConsume(List<String> apduList);

    protected abstract RET finalResult();

    protected final <TYPE> ApduResult/* <TYPE> */ nextProvide(TYPE obj) {
        return new ApduResult(mProvideStep, obj);
    }

    protected final <TYPE> ApduResult nextTransmit(TYPE obj) {
        return new ApduResult(mTransmitStep, obj);
    }

    protected final <TYPE> ApduResult nextConsume(TYPE obj) {
        return new ApduResult(mConsumeStep, obj);
    }

    protected final <TYPE> ApduResult nextFinal(TYPE obj) {
        return new ApduResult(mFinalStep, obj);
    }
}

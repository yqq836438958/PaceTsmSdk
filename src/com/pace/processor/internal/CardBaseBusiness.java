
package com.pace.processor.internal;

import com.pace.processor.APDU;
import com.pace.processor.IApduProvider;
import com.pace.step.Step;

import java.util.HashMap;
import java.util.List;

public abstract class CardBaseBusiness {
    enum APDU_STEP {
        PREPARE, APDU_PROVIDE, APDU_TRANSIMT, APDU_CONSUME, FINAL,
    }

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

    public String onCall(String input) {
        mPrepareStep.onStepEnter();
        // TODO ...
        if (mFinalStep.isCurrentStep()) {
        }
        return finalResult();
    }

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

    protected abstract ApduResult<Boolean> onCachPrepare();

    protected abstract ApduResult<APDU> onApduProvide(Object input);

    protected abstract ApduResult<APDU> onApduConsume(List<String> apduList);

    protected abstract String finalResult();

    protected final ApduResult nextProvide(Object obj) {
        return new ApduResult(APDU_STEP.APDU_PROVIDE, obj);
    }

    protected final ApduResult nextTransmit(Object obj) {
        return new ApduResult(APDU_STEP.APDU_TRANSIMT, obj);
    }

    protected final ApduResult nextConsume(Object obj) {
        return new ApduResult(APDU_STEP.APDU_PROVIDE, obj);
    }

    protected final ApduResult nextFinal(Object obj) {
        return new ApduResult(APDU_STEP.FINAL, obj);
    }
}

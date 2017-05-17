
package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.APDU_STEP;
import com.pace.processor.internal.base.ApduChainController.ApduChainNode;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.ApduStep;
import com.pace.processor.internal.base.IApduProvider;

import java.util.List;

public abstract class CardBaseBusiness extends ApduChainNode {

    protected IApduProvider mApduProvider = null;
    private ApduStep mCurStep = null;
    private RET mFinalRet = RET.empty();
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
            String input = (String) getParam();
            ApduResult<APDU> result = onPrepare(input);
            result.call(this);
        }
    };
    private ApduStep mConsumeStep = new ApduStep(APDU_STEP.APDU_CONSUME) {

        @Override
        public void onStepHandle() {
            // TODO Auto-generated method stub

        }

    };

    public CardBaseBusiness() {
    }

    @Override
    public RET onCall(String input) {
        mPrepareStep.setParam(input);
        mPrepareStep.onStepEnter();
        // TODO ...
        if (mFinalStep.isCurrentStep()) {
        }
        return mFinalRet;
    }

    protected abstract ApduResult onPrepare(String source);

    protected abstract ApduResult onApduProvide(Object input);

    protected abstract ApduResult onApduConsume(List<String> apduList);

    protected final <TYPE> ApduResult<TYPE> nextProvide(TYPE obj) {
        return new ApduResult<TYPE>(mProvideStep, obj);
    }

    protected final <TYPE> ApduResult<TYPE> nextTransmit(TYPE obj) {
        return new ApduResult<TYPE>(mTransmitStep, obj);
    }

    protected final <TYPE> ApduResult<TYPE> nextConsume(TYPE obj) {
        return new ApduResult<TYPE>(mConsumeStep, obj);
    }

    protected final <TYPE> ApduResult<TYPE> nextFinal(TYPE obj) {
        return new ApduResult<TYPE>(mFinalStep, obj);
    }
}

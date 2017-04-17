
package com.pace.step;

/**
 * @author baodingzhou
 */

public abstract class Step<STEP> implements IStep<STEP> {

    protected STEP mStep = null;

    protected STATUS mStatus = STATUS.QUIT;

    public Step(STEP step) {
        mStep = step;
    }

    @Override
    public final STEP getStep() {
        return mStep;
    }

    @Override
    public final void onStepEnter() {
        mStatus = STATUS.ENTER;
        notifyStepStatus(mStep, mStatus);
        onStep();
    }

    private final void onStep() {
        if (isCurrentStep() && mStatus != STATUS.HANDLE) {
            mStatus = STATUS.HANDLE;
            notifyStepStatus(mStep, mStatus);
            onStepHandle();
        }
    }

    @Override
    public final void keepStep() {
        mStatus = STATUS.KEEP;
        notifyStepStatus(mStep, mStatus);
    }

    private void onStepQuit() {
        mStatus = STATUS.QUIT;
        notifyStepStatus(mStep, mStatus);
    }

    @Override
    public final boolean isCurrentStep() {
        return mStatus != STATUS.QUIT;
    }

    @Override
    public final STATUS getStatus() {
        return mStatus;
    }

    @Override
    public boolean repeatStep() {
        if (isCurrentStep()) {
            return setStepInternal(this);
        }
        return false;
    };

    @Override
    public boolean switchStep(IStep<STEP> step) {
        if (isCurrentStep()) {
            return setStepInternal(step);
        }
        return false;
    }

    private boolean setStepInternal(IStep<STEP> step) {
        boolean handle = false;
        if (step != null) {
            this.onStepQuit();
            step.onStepEnter();
            handle = true;
        }
        return handle;
    }

    protected void notifyStepStatus(STEP step, STATUS status) {

    }
}

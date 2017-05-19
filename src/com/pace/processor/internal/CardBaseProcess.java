
package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.internal.base.IApduProvider;
import com.pace.processor.internal.base.ApduChainController.ApduChainNode;
import com.pace.processor.internal.state.IProcessAction;
import com.pace.processor.internal.state.ProcessContext;

public abstract class CardBaseProcess extends ApduChainNode {
    protected IApduProvider mApduProvider = null;

    @Override
    protected RET onCall(String msg) {
        ProcessContext context = new ProcessContext(msg);
        mPrepareAction.onCall(context);
        RET finalRet = context.getOutPut();
        if (finalRet.getCode() == RET.RET_IGONRE) {
            // IGONRE代表已经拿到结果了，我们认为这种情况也是成功的
            return RET.suc(finalRet.getMsg());
        }
        return finalRet;
    }

    private IProcessAction mPrepareAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return filter(onPostHandle(context), context, mProviderAction);
        }
    };
    private IProcessAction mProviderAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return filter(onPostHandle(context), context, mTransmitAction);
        }
    };
    private IProcessAction mTransmitAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return filter(onTransmit(context), context, mPostHandleAction);
        }
    };
    private IProcessAction mPostHandleAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return filter(onPostHandle(context), context, mProviderAction);
        }
    };
    private IProcessAction mFinalAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return context.getOutPut().getCode();
        }
    };

    private int filter(int ret, ProcessContext context, IProcessAction newAction) {
        context.setCode(ret);
        IProcessAction realAction = (ret == 0 ? newAction : mFinalAction);
        context.setAction(realAction);
        return realAction.onCall(context);
    }

    protected abstract int onPrepare(ProcessContext context);

    protected abstract int onProvider(ProcessContext context);

    private int onTransmit(ProcessContext context) {
        return 0;
    }

    protected abstract int onPostHandle(ProcessContext context);
}

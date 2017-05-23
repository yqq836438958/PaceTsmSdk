
package com.pace.processor.internal;

import com.pace.common.ErrCode;
import com.pace.common.RET;
import com.pace.processor.internal.base.IApduProvider;
import com.pace.processor.APDU;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.channel.ApduChannel;
import com.pace.processor.internal.base.ApduChainController.ApduChainNode;
import com.pace.processor.internal.state.IProcessAction;
import com.pace.processor.internal.state.ProcessContext;

import java.util.ArrayList;
import java.util.List;

public abstract class CardBaseProcess extends ApduChainNode {
    protected IApduProvider mApduProvider = null;

    @Override
    protected RET onCall(ParamBean msg) {
        ProcessContext context = new ProcessContext(msg);
        mPrepareAction.onCall(context);
        RET finalRet = context.getOutPut();
        if (finalRet.getCode() == RET.RET_OVER) {
            // RET_OVER代表已经拿到结果了，我们认为这种情况也是成功的
            return RET.suc(finalRet.getMsg());
        }
        return finalRet;
    }

    private IProcessAction mPrepareAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return next(onPrepare(context), context, mProviderAction);
        }
    };
    private IProcessAction mProviderAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return next(onProvider(context), context, mTransmitAction);
        }
    };
    private IProcessAction mTransmitAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            return next(onTransmit(context), context, mPostHandleAction);
        }
    };
    private IProcessAction mPostHandleAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            List<String> apduList = (List<String>) context.getParam();
            if (apduList == null || apduList.size() <= 0) {
                return next(ErrCode.ERR_LOCAL_APDU_NULL, context, mFinalAction);
            }
            return next(onPostHandle(context, apduList), context, mProviderAction);
        }
    };
    private IProcessAction mFinalAction = new IProcessAction() {

        @Override
        public int onCall(ProcessContext context) {
            ApduChannel.get().close();
            return context.getOutPut().getCode();
        }
    };

    private int next(int ret, ProcessContext context, IProcessAction newAction) {
        context.setCode(ret);
        IProcessAction realAction = (ret == RET.RET_NEXT ? newAction
                : (ret == RET.RET_REFRESH ? mProviderAction : mFinalAction));
        context.setAction(realAction);
        return realAction.onCall(context);
    }

    protected abstract int onPrepare(ProcessContext context);

    protected abstract int onProvider(ProcessContext context);

    private int onTransmit(ProcessContext context) {
        Object param = context.getParam();
        if (param instanceof APDU) {
            List<String> source = ((APDU) param).getData();
            if (source == null || source.size() <= 0) {
                return ErrCode.ERR_LOCAL_APDU_NULL;
            }
            List<String> rsp = new ArrayList<String>();
            int ret = ApduChannel.get().transmit(source, rsp);
            if (ret != 0) {
                return ret;
            }
            if (rsp == null || rsp.size() <= 0) {
                return ErrCode.ERR_RSP_APDU_NULL;
            }
        }
        return ErrCode.ERR_LOCAL_APDU_NULL;
    }

    protected abstract int onPostHandle(ProcessContext context, List<String> apduList);
}

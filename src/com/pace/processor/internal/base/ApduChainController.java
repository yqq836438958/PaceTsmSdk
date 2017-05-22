
package com.pace.processor.internal.base;

import com.pace.common.RET;
import com.pace.processor.bean.ParamBean;

import java.util.ArrayList;
import java.util.List;

public class ApduChainController {
    private List<IApduChainNode> mList = new ArrayList<IApduChainNode>();
    private int mSize = 0;

    interface IApduChainNode {
        public RET call(ParamBean msg);

        public void next(IApduChainNode node);
    }

    public void add(IApduChainNode chain) {
        if (chain == null) {
            return;
        }
        synchronized (ApduChainController.class) {
            if (mSize > 0) {
                mList.get(mSize - 1).next(chain);
            }
            mList.add(chain);
            mSize++;
        }
    }

    public final RET invoke(ParamBean msg) {
        if (mList.size() <= 0) {
            return RET.err("");
        }
        return mList.get(0).call(msg);
    }

    public static abstract class ApduChainNode implements IApduChainNode {

        protected abstract RET onCall(ParamBean msg);

        private IApduChainNode nextChain = null;

        @Override
        public RET call(ParamBean msg) {
            RET result = onCall(msg);
            if (RET.isError(result) || nextChain == null) {
                return result;
            }
            return nextChain.call(msg);
        }

        @Override
        public void next(IApduChainNode next) {
            nextChain = next;
        }
    }
}


package com.pace.processor.internal.state;

import com.pace.common.RET;
import com.pace.processor.bean.ParamBean;

public class ProcessContext {
    private Object nextObject;
    private IProcessAction mState = null;
    private ParamBean sInput = null;
    private RET outRet = null;

    public ProcessContext(ParamBean src) {
        sInput = src;
        outRet = RET.empty();
    }

    public ParamBean getSource() {
        return sInput;
    }

    public IProcessAction getAction() {
        return mState;
    }

    public void setCode(int err) {
        outRet.setCode(err);
    }

    public void setAction(final IProcessAction action) {
        mState = action;
    }

    public void setParam(Object obj) {
        nextObject = obj;
    }

    public Object getParam() {
        return nextObject;
    }

    public void setOutPut(String ret) {
        outRet.setMsg(ret);
    }

    public RET getOutPut() {
        return outRet;
    }
}

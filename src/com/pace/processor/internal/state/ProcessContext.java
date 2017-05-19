
package com.pace.processor.internal.state;

import com.pace.common.RET;

public class ProcessContext {
    private Object nextObject;
    private IProcessAction mState = null;
    private String sInput = "";
    private RET outRet = RET.empty();

    public ProcessContext(String src) {
        sInput = src;
    }

    public String getSource() {
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

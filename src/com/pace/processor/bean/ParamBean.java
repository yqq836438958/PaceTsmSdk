
package com.pace.processor.bean;

public class ParamBean {
    private String src = null;
    private int iType = -1;

    public ParamBean(String msg) {
        this.src = msg;
    }

    public ParamBean(String msg, int type) {
        this.src = msg;
        iType = type;
    }

    public String getData() {
        return src;
    }

    public int getType() {
        return iType;
    }
}

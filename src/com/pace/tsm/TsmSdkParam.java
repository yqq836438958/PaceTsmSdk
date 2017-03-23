
package com.pace.tsm;

public class TsmSdkParam {
    private int iOperation;

    public int getOperation() {
        return iOperation;
    }

    private void setOperation(int operation) {
        iOperation = operation;
    }

    public static TsmSdkParam newEmptyParam(int type) {
        TsmSdkParam param = new TsmSdkParam();
        param.setOperation(type);
        return param;
    }

    public static TsmSdkParam wrap(String input) {
        return new TsmSdkParam();
    }
}

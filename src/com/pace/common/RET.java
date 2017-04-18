
package com.pace.common;

public class RET {
    private int iRet;
    private String sMsg;

    private RET(int ret, String str) {
        iRet = ret;
        sMsg = str;
    }

    public static boolean isError(RET ret) {
        return ret.iRet != 0;
    }

    public static RET suc(String str) {
        return new RET(0, str);
    }

    public static RET error(int err, String errStr) {
        return new RET(err, errStr);
    }

    public static RET err(int err) {
        return new RET(err, "");
    }

    public static RET err(String err) {
        return new RET(ErrCode.ERR_COMMON, err);
    }

}

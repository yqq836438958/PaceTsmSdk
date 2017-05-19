
package com.pace.common;

public class RET {
    public static final int RET_IGONRE = 1;
    private int iRet;
    private String sMsg;

    private RET(int ret, String str) {
        iRet = ret;
        sMsg = str;
    }

    public int getCode() {
        return iRet;
    }

    public void setCode(int code) {
        iRet = code;
    }

    public void setMsg(String msg) {
        sMsg = msg;
    }

    public String getMsg() {
        return sMsg;
    }

    public static boolean isError(RET ret) {
        return ret.iRet != 0;
    }

    public static RET newObj(int i, String str) {
        return new RET(i, str);
    }

    public static RET suc(String str) {
        return newObj(0, str);
    }

    public static RET error(int err, String errStr) {
        return newObj(err, errStr);
    }

    public static RET err(int err) {
        return newObj(err, "");
    }

    public static RET err(String err) {
        return newObj(ErrCode.ERR_COMMON, err);
    }

    public static RET empty() {
        return RET.err("");
    }
}

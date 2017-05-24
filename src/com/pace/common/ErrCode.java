
package com.pace.common;

public class ErrCode {
    public static final int ERR_COMMON = -1;
    public static final int ERR_LOCAL_APDU_NULL = -2;
    public static final int ERR_NET_APDU_NULL = -3;
    public static final int ERR_RSP_APDU_NULL = -4;
    public static final int ERR_RSP_APDU_FAIL = -5;

    public static final int ERR_CARDLIST_NULL = 10001; // 列表为空
    // 设备端错误
    public static final int ERR_APDU_REQ_TIMEOUT = -6;
    public static final int ERR_THREAD_ERR = -7;
    public static final int ERR_UNKOWN_ERR = -8;
}

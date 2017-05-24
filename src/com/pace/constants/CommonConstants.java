
package com.pace.constants;

public class CommonConstants {

    public static final int WALLET_ACCOUNT_AUTH_FAILED = -101;
    public static final int WALLET_QUERY_MAX_TIMES = 3;
    public static final int TASK_CARD_CPLC = 101;
    public static final int TASK_CARD_LIST = 102;
    public static final int TASK_CARD_QUERY = 103;
    public static final int TASK_CARD_SWITCH = 104;
    public static final int TASK_CARD_NET_BUSINESS = 105;
    public static final int TASK_NET_SE_INIT = 106;

    public static final String INSTANCE_ID = "instance_id";

    public static enum NET_BUSINESS_TYPE {
        TYPE_ISSUECARD(1), TYPE_TOPUP(2), TYPE_DELETE(3), TYPE_SEINIT(4);
        private int val;

        private NET_BUSINESS_TYPE(int val) {
            this.val = val;
        }
    }

}

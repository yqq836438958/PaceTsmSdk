
package com.pace.tsm;

import android.content.Context;

import com.pace.api.IApduChannel;
import com.pace.constants.CommonConstants;

import java.util.Arrays;
import java.util.List;

public class TsmApi {
    public static final int API_RUN_CROSS_DEV = 0;
    public static final int API_RUN_LOCAL = 1;
    private static Context sContext = null;

    public static Context getGlobalContext() {
        return sContext;
    }

    public static void regist(Context context, IApduChannel apduChannel) {
        sContext = context;
    }

    public static String issueCard(String input) {
        return invokeCardBusiness(input);
    }

    private static String invokeCardBusiness(String input) {
        TsmLauncher launcher = TsmLauncher.get();
        List<Integer> routList = Arrays.asList(CommonConstants.TASK_CARD_CPLC,
                CommonConstants.TASK_CARD_NET_BUSINESS);
        long reqId = launcher.sendReq(input, CommonConstants.TASK_CARD_NET_BUSINESS, routList);
        return launcher.waitRsp(reqId);
    }

    public static String topupCard(String input) {
        return invokeCardBusiness(input);
    }

    public static String cardListQuery() {
        return null;
    }

    public static String cardQuery() {
        return null;
    }

    public static String cardSwitch(String aid) {
        return null;
    }

}

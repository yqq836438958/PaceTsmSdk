
package com.pace.tsm;

import android.content.Context;

import com.pace.api.IApduChannel;
import com.pace.common.RET;
import com.pace.processor.Dispatcher.CardCplcType;
import com.pace.processor.Dispatcher.CardListQueryType;
import com.pace.processor.Dispatcher.CardNetBusinessType;
import com.pace.processor.Dispatcher.CardQueryType;
import com.pace.processor.Dispatcher.CardSwitchType;
import com.pace.processor.Dispatcher.IBusinessType;

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

    public static int cardIssue(String input, String[] output) {
        return invokeInteranl(new CardNetBusinessType(), input, output);
    }

    public static int cardTopup(String input, String[] output) {
        return invokeInteranl(new CardNetBusinessType(), input, output);
    }

    public static int cardListQuery(String[] output) {
        return invokeInteranl(new CardListQueryType(), "", output);
    }

    public static int cardQuery(String input, String[] output) {
        return invokeInteranl(new CardQueryType(), input, output);
    }

    public static int cardSwitch(String aid) {
        return invokeInteranl(new CardSwitchType(), aid, null);
    }

    public static int cardCplc(String[] output) {
        return invokeInteranl(new CardCplcType(), "", output);
    }

    private static RET invokeCardInnerRet(String input, IBusinessType type) {
        TsmLauncher launcher = TsmLauncher.get();
        long lreq = launcher.sendReq(input, type);
        RET result = launcher.waitRsp(lreq);
        return result;
    }

    private static int parseExeResult(RET ret, String[] output) {
        if (output != null && output.length > 0) {
            output[0] = ret.getMsg();
        }
        return ret.getCode();
    }

    private static int invokeInteranl(IBusinessType type, String input, String[] output) {
        RET ret = invokeCardInnerRet(input, type);
        return parseExeResult(ret, output);
    }
}

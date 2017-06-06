
package com.pace.tsm;

import com.pace.common.RET;
import com.pace.common.ThreadExecutors;
import com.pace.constants.CommonConstants.NET_BUSINESS_TYPE;
import com.pace.processor.Dispatcher.CardCplcType;
import com.pace.processor.Dispatcher.CardListQueryType;
import com.pace.processor.Dispatcher.CardNetBusinessType;
import com.pace.processor.Dispatcher.CardQueryType;
import com.pace.processor.Dispatcher.CardSwitchType;
import com.pace.processor.Dispatcher.CardTransactionType;
import com.pace.processor.Dispatcher.IBusinessType;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.channel.ApduChannel;
import com.pace.tsm.service.IPaceApduChannel;

public class TsmApiAsync {
    public static interface ITsmApiCallback {
        void onCallback(int code, String result);
    }

    public static void regist(IPaceApduChannel apduChannel) {
        ApduChannel.get().setChannel(TsmApp.getAppContext(), apduChannel);
    }

    public static void cardCplc(ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean("");
        invokeBusinessInternal(newInput, new CardCplcType(), callback);
    }

    public static void cardQuery(String input, ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean(input);
        invokeBusinessInternal(newInput, new CardQueryType(), callback);
    }

    public static void cardListQuery(ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean("");
        invokeBusinessInternal(newInput, new CardListQueryType(), callback);
    }

    public static void cardSwitch(String input, ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean(input);
        invokeBusinessInternal(newInput, new CardSwitchType(), callback);
    }

    public static void cardIssue(String input, ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean(input, NET_BUSINESS_TYPE.TYPE_ISSUECARD.ordinal());
        invokeBusinessInternal(newInput, new CardNetBusinessType(), callback);
    }

    public static void cardTopup(String input, ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean(input, NET_BUSINESS_TYPE.TYPE_TOPUP.ordinal());
        invokeBusinessInternal(newInput, new CardNetBusinessType(), callback);
    }

    public static void cardTransaction(String input, ITsmApiCallback callback) {
        ParamBean newInput = new ParamBean(input);
        invokeBusinessInternal(newInput, new CardNetBusinessType(), callback);
    }

    private static long sendBusinessReq(ParamBean input, IBusinessType type) {
        TsmLauncher launcher = TsmLauncher.get();
        return launcher.sendReq(input, type);
    }

    private static void invokeBusinessInternal(ParamBean input, IBusinessType type,
            ITsmApiCallback callback) {
        long lReq = sendBusinessReq(input, type);
        if (lReq < 0) {
            callback.onCallback(-1, "error request");
            return;
        }
        sendAsyncCallback(lReq, callback);
    }

    private static void sendAsyncCallback(final long lReq, final ITsmApiCallback callback) {
        Runnable rspRun = new Runnable() {

            @Override
            public void run() {
                TsmLauncher launcher = TsmLauncher.get();
                RET result = launcher.waitRsp(lReq);
                if (callback != null) {
                    callback.onCallback(result.getCode(), result.getMsg());
                }
            }
        };
        ThreadExecutors.background().execute(rspRun);
    }
}

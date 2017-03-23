
package com.pace.tsm;

import android.content.Context;

import com.pace.api.IApduChannel;

public class TsmApi {
    public static void regist(Context context, IApduChannel apduChannel) {

    }

    public static String issueCard(String input) {
        TsmSdkParam param = TsmSdkParam.wrap(input);
        TsmLauncher.get().main(param);
        return null;
    }

    public static String topupCard(String input) {
        return null;
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

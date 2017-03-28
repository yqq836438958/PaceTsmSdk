
package com.pace.tsm;

import android.content.Context;

import com.pace.api.IApduChannel;

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
        TsmLauncher.get().main(input);
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

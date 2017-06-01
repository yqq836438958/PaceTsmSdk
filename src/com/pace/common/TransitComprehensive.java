
package com.pace.common;

import com.pace.tsm.plugin.utils.LogUtil;
import com.pace.tsm.plugin.utils.ValueUtil;

public class TransitComprehensive {
    private static String TAG = "TransitComprehensive";

    public static String getCardNumber(String str) {
        String str2 = "";
        if (ValueUtil.isEmpty(str)) {
            return str2;
        }
        try {
            if (str.endsWith("9000")) {
                return str.substring(0, str.length() - 4);
            }
            str2 = "";
            LogUtil.loge(TAG, str + " is not right ,cardNumber ");
            return str2;
        } catch (Exception e) {
            LogUtil.loge(TAG, str + " is invalid ,cardNumber ");
            return str2;
        }
    }

    public static String getCardExpire(String str) {
        String str2 = "";
        if (ValueUtil.isEmpty(str)) {
            return str2;
        }
        str2 = str.substring(0, str.length() - 4);
        try {
            return str2.substring(0, 8) + "-" + str2.substring(8, 16);
        } catch (Exception e) {
            e.printStackTrace();
            str2 = "";
            LogUtil.loge(TAG, str + " is invalid ,cardExpire ");
            return str2;
        }
    }

    public static String getBalance(String str) {
        String str2 = "";
        if (!str.endsWith("9000")) {
            return str2;
        }
        try {
            return (((double) Long.parseLong(str.substring(0, str.length() - 4), 16)) / 100.0d)
                    + "";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            str2 = "0";
            LogUtil.loge(TAG, " covert balance failure,because of  " + str + " is invalid ");
            return str2;
        }
    }

    public static String getBalanceFen(String str) {
        String str2 = "0";
        if (!str.endsWith("9000")) {
            return str2;
        }
        try {
            return Long.parseLong(str.substring(0, str.length() - 4), 16) + "";
        } catch (Exception e) {
            str2 = "0";
            LogUtil.loge(TAG, " covert balance failure,because of  " + str + " is invalid ");
            return str2;
        }
    }

    public static String getAmountFen(String str) {
        String str2 = "0";
        try {
            return Long.parseLong(str.replaceAll("^0*", ""), 16) + "";
        } catch (Exception e) {
            str2 = "0";
            LogUtil.loge(TAG, " covert amount failure,because of  " + str + " is invalid ");
            return str2;
        }
    }

    public static String getBalanceInOuterDev(String str) {
        String str2 = "";
        if (!str.endsWith("9000")) {
            return str2;
        }
        try {
            return Long.parseLong(str.substring(0, str.length() - 4), 16) + "";
        } catch (NumberFormatException e) {
            e.printStackTrace();
            str2 = "0";
            LogUtil.loge(TAG, " covert balance failure,because of  " + str + " is invalid ");
            return str2;
        }
    }

}

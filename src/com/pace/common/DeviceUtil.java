
package com.pace.common;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DeviceUtil {
    public static String getVendor() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getIMEI(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getApiVer() {
        return "1.0";
    }
}

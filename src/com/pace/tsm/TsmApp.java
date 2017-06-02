
package com.pace.tsm;

import android.app.Application;
import android.content.Context;

public class TsmApp extends Application {
    private static Context sGlobalCtx = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Context ctx = getApplicationContext();
        sGlobalCtx = ctx;
    }

    public static Context getAppContext() {
        return sGlobalCtx;
    }
}


package com.pace.plugin;

public class PluginManager {
    private static volatile PluginManager sInstance = null;
    private ICardPluginService mService = null;

    public static PluginManager getInstance() {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager();
                }
            }
        }
        return sInstance;
    }

    public ICardPluginService getService() {
        return mService;
    }

}

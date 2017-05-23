
package com.pace.cache;

import com.pace.processor.internal.base.NetApduData;

public class TsmCache {
    private static final String CACHE_FILE = "tsm_cache_file";
    private static final String KEY_CPLC_PREFIX = "tsm_key_cplc";
    public static final String KEY_CMD = "file_net_cmd_key_";
    private static TsmCache sInstance = null;

    public static TsmCache get() {
        if (sInstance == null) {
            synchronized (TsmCache.class) {
                sInstance = new TsmCache();
            }
        }
        if (!CACHE_FILE.equalsIgnoreCase(CacheUtils.sShareFile)) {
            CacheUtils.sShareFile = CACHE_FILE;
        }
        return sInstance;
    }

    public void saveCplc(String cplc) {
        CacheUtils.save(KEY_CPLC_PREFIX, cplc);
    }

    public String getCplc() {
        return CacheUtils.get(KEY_CPLC_PREFIX, "");
    }

    public String getCardList() {
        String key = getSimpleCplc();
        return CacheUtils.get(key, "");
    }

    private String getSimpleCplc() {
        String cplc = getCplc();
        String simpleCplc = cplc.substring(24, 36);
        return simpleCplc;
    }

    public void saveCardList(String list) {
        String key = getSimpleCplc();
        CacheUtils.save(key, list);
    }

    public void saveLastExeRet(int key, NetApduData data) {
        CacheUtils.save(KEY_CMD + key, data);
    }

    public NetApduData getLastExeRet(int key) {
        return (NetApduData) CacheUtils.get(KEY_CMD + key);
    }
}

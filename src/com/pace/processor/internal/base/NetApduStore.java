
package com.pace.processor.internal.base;

import com.pace.cache.TsmCache;
import com.pace.constants.CommonConstants.NET_BUSINESS_TYPE;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NetApduStore {
    public static final String FILE_NET_APDU = "file_net_apdu_";
    public static final String KEY_CMD = "file_net_cmd_key_";
    private ConcurrentHashMap<Integer, NetApduData> mLastNetApduDatMap = new ConcurrentHashMap<Integer, NetApduData>();
    private static NetApduStore sInstance = null;

    public static NetApduStore get() {
        if (sInstance == null) {
            synchronized (NetApduStore.class) {
                if (sInstance == null) {
                    sInstance = new NetApduStore();
                }
            }
        }
        return sInstance;
    }

    public NetApduStore() {
        loadDataFromDisk();
    }

    private void loadDataFromDisk() {
        for (NET_BUSINESS_TYPE _type : NET_BUSINESS_TYPE.values()) {
            int type = _type.ordinal();
            NetApduData cmd = TsmCache.get().getLastExeRet(type);
            if (cmd != null) {
                mLastNetApduDatMap.put(type, cmd);
            }
        }
    }

    public void append(int type, List<String> rsp) {
        NetApduData data = mLastNetApduDatMap.get(type);
        data.rsp = rsp;
        TsmCache.get().saveLastExeRet(type, data);
    }

    public void add(int type, NetApduData data) {
        mLastNetApduDatMap.put(type, data);
        TsmCache.get().saveLastExeRet(type, data);
    }

    public NetApduData getLastData(Integer type) {
        return mLastNetApduDatMap.get(type);
    }
}

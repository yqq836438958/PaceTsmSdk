
package com.pace.processor.internal.base;

import android.R.integer;

import com.pace.cache.CacheUtils;
import com.pace.cache.TsmCache;
import com.pace.constants.CommonConstants.NET_BUSINESS_TYPE;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NetApduStore {
    public static final String FILE_NET_APDU = "file_net_apdu_";
    public static final String KEY_CMD = "file_net_cmd_key_";
    public static final String KEY_RSP = "file_net_rsp_key_";
    // private ConcurrentHashMap<Integer, NetApduCmd> mLastNetApduCmdMap = new
    // ConcurrentHashMap<Integer, NetApduCmd>();
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
        String cplc = TsmCache.getCplc();
        loadDataFromDisk(cplc);
    }

    private void loadDataFromDisk(String cplc) {
        String simpleCplc = cplc.substring(24, 36);
        CacheUtils.prepareFile(FILE_NET_APDU + simpleCplc);
        for (NET_BUSINESS_TYPE _type : NET_BUSINESS_TYPE.values()) {
            int type = _type.ordinal();
            NetApduData cmd = (NetApduData) CacheUtils.get(KEY_CMD + type);
            // if (cmd != null) {
            // mLastNetApduCmdMap.put(type, cmd);
            // }
            if (cmd != null) {
                mLastNetApduDatMap.put(type, cmd);
            }
        }
        CacheUtils.cleanFile();
    }

    // public void saveCmd(int type, NetApduCmd cmd) {
    // mLastNetApduCmdMap.put(type, cmd);
    // }
    public void append(int type, List<String> rsp) {
        NetApduData data = mLastNetApduDatMap.get(type);
        data.rsp = rsp;
    }

    public void add(int type, NetApduData data) {
        mLastNetApduDatMap.put(type, data);
    }

    public NetApduData getLastData(Integer type) {
        return mLastNetApduDatMap.get(type);
    }
}


package com.pace.tsm;

import com.pace.common.RET;
import com.pace.event.EventEngineImp;
import com.pace.event.IEventEngine;
import com.pace.processor.Dispatcher.IBusinessType;
import com.pace.processor.bean.ParamBean;

class TsmLauncher {

    private static TsmLauncher sInstance = null;
    private IEventEngine mTaskEngine = null;

    public static TsmLauncher get() {
        if (sInstance == null) {
            synchronized (TsmLauncher.class) {
                sInstance = new TsmLauncher();
            }
        }
        return sInstance;
    }

    private TsmLauncher() {
        mTaskEngine = new EventEngineImp();
    }

    public long sendReq(ParamBean param, IBusinessType type) {
        return mTaskEngine.offer(param, type);
    }

    public RET waitRsp(long lReqId) {
        return mTaskEngine.take(lReqId);
    }
}

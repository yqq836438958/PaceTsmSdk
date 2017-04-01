
package com.pace.tsm;

import com.pace.event.ITaskEngine;
import com.pace.event.PidRouter;
import com.pace.event.TaskEngineImp;
import com.pace.event.TaskEventSource;

import java.util.List;

public class TsmLauncher {

    private static TsmLauncher sInstance = null;
    private ITaskEngine mTaskEngine = null;

    public static TsmLauncher get() {
        if (sInstance == null) {
            synchronized (sInstance) {
                sInstance = new TsmLauncher();
            }
        }
        return sInstance;
    }

    private TsmLauncher() {
        mTaskEngine = new TaskEngineImp();
    }

    public long sendReq(String param, int pid, List<Integer> routeList) {
        TaskEventSource inputSource = TaskEventSource.wrap(pid, param);
        PidRouter router = PidRouter.newRouter(routeList);
        return mTaskEngine.addTask(inputSource, router);
    }

    public String waitRsp(long lReqId) {
        return null;
    }
}

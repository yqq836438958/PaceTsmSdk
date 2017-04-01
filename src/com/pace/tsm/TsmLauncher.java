
package com.pace.tsm;

import com.pace.event.ITask;
import com.pace.event.ITaskEngine;
import com.pace.event.TaskEngineImp;

import java.util.ArrayList;
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
        init();
    }

    private void init() {
        List<ITask> list = new ArrayList<ITask>();
        // list.add(TASK_APDU);
        // list.add(TASK_WUP);
        // mTaskEngine = new TaskEngine(list);
    }

    public void main(String param) {
        // startEngine(param);
    }

    private void startEngine() {
        // TASK_APDU.setProcess(process);
        // TASK_WUP.setProcess(process);
        // mTaskEngine.start();
    }
}

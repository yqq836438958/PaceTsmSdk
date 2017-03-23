
package com.pace.tsm;

import com.event.CommonTask;
import com.event.IBaseProcess;
import com.event.ITask;
import com.event.ITaskEngine;
import com.event.TaskEngine;
import com.pace.apdutransmit.ApduProcessFactory;
import com.pace.events.ApduProcess;

import java.util.ArrayList;
import java.util.List;

public class TsmLauncher {

    private static TsmLauncher sInstance = null;
    private ITaskEngine mTaskEngine = null;
    private final ITask TASK_APDU = new CommonTask();
    private final ITask TASK_WUP = new CommonTask();

    public static TsmLauncher get() {
        if (sInstance == null) {
            synchronized (sInstance) {
                sInstance = new TsmLauncher();
            }
        }
        return sInstance;
    }

    private TsmLauncher() {
        init();
        prepareCPLC();
    }

    private void init() {
        List<ITask> list = new ArrayList<ITask>();
        list.add(TASK_APDU);
        list.add(TASK_WUP);
        mTaskEngine = new TaskEngine(list);
    }

    private void prepareCPLC() {
        TsmSdkParam cplcParm = TsmSdkParam.newEmptyParam(ApduProcess.TASK_CARDCPLC);
        startEngine(cplcParm);
    }

    public void main(TsmSdkParam param) {
        startEngine(param);
    }

    private void startEngine(TsmSdkParam param) {
        mTaskEngine.prepare();
        IBaseProcess process = ApduProcessFactory.get().newProcess(param);
        TASK_APDU.setProcess(process);
        TASK_WUP.setProcess(process);
        mTaskEngine.start();
    }
}

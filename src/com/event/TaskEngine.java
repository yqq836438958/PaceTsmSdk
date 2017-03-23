
package com.event;

import com.log.LogPrint;

import java.util.ArrayList;
import java.util.List;

public class TaskEngine implements ITaskEngine {
    private List<ITask> mTaskList = new ArrayList<ITask>();
    private volatile boolean isRunning = false;
    private static Object sLock = new Object();

    public TaskEngine(List<ITask> listTask) {
        mTaskList = listTask;
    }

    private TaskResult runEngine() {
        synchronized (sLock) {
            ITask task = null;
            int index = 0;
            TaskResult execRet = TaskResult.emptyResult();
            LogPrint.d("loop begin");
            while (isRunning) {
                switch (execRet.getRet()) {
                    case TaskResult.TASK_FINISH:
                        isRunning = false;
                        break;
                    case TaskResult.TASK_SWITH:
                        index++;
                        if (index >= mTaskList.size()) {
                            index = 0;
                        }
                        break;
                    default:
                        break;
                }
                task = mTaskList.get(index);
                if (task != null) {
                    execRet = task.exec();
                }
            }
            sLock.notifyAll();
            LogPrint.d("quit loop now");
            return execRet;
        }
    }

    @Override
    public TaskResult start() {
        waitForTaskFinish();
        return runEngine();
    }

    private void waitForTaskFinish() {
        synchronized (sLock) {
            if (isRunning) {
                LogPrint.d("lock need wait");
                try {
                    sLock.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void prepare() {
        waitForTaskFinish();
    }
}

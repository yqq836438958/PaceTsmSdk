
package com.event;

import com.log.LogPrint;

import java.util.ArrayList;
import java.util.List;

public class TaskEngine implements ITaskEngine {
    private List<ITask> mTaskList = new ArrayList<ITask>();
    private volatile boolean isRunning = false;
    private static Object sLock = new Object();

    private TaskResult runEngine() {
        ITask task = null;
        int index = 0;
        TaskResult execRet = TaskResult.emptyResult();
        LogPrint.d("loop begin");
        synchronized (sLock) {
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
                execRet = task.exec();
            }
        }
        sLock.notifyAll();
        LogPrint.d("quit loop now");
        return execRet;
    }

    @Override
    public void regist(ITask task) {
        synchronized (sLock) {
            mTaskList.add(task);
        }
    }

    @Override
    public TaskResult start() {
        waitForTaskFinish();
        return runEngine();
    }

    private void waitForTaskFinish() {
        if (isRunning) {
            LogPrint.d("lock need wait");
            synchronized (sLock) {
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

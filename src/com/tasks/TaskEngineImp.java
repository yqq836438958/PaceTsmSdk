
package com.tasks;

import com.event.ITask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEngineImp {
    private LinkedBlockingQueue<ITask> mTaskQueue = new LinkedBlockingQueue<ITask>();
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);

    public TaskEngineImp() {

    }

    public void prepare() {
        TaskHandler handler = new TaskHandler();
        while (mIsRunning.get()) {
            ITask task = null;
            try {
                task = mTaskQueue.take();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (task == null) {
                mIsRunning.set(false);
                break;
            }
            task.exec();
            handler.handle();
        }
    }

    public void run() {

    }

    public interface IResultHandler {
        public void onHandle(LinkedBlockingQueue<ITask> queue);
    }

    public class TaskHandler {
        IResultHandler mHandler = null;

        public void set(IResultHandler handler) {
            mHandler = handler;
        }

        public void handle() {
            mHandler.onHandle(mTaskQueue);
        }
    }

    public static class ChangeProcessHandler implements IResultHandler {

        @Override
        public void onHandle(LinkedBlockingQueue<ITask> queue) {

        }
    }

    public class RepeatProcessHandler implements IResultHandler {

        @Override
        public void onHandle(LinkedBlockingQueue<ITask> queue) {
            // TODO Auto-generated method stub
            ITask task = new BaseTask();
            queue.add(task);
        }

    }

    public class DefaultTaskHandler implements IResultHandler {

        @Override
        public void onHandle(LinkedBlockingQueue<ITask> queue) {
            // TODO Auto-generated method stub

        }

    }

    public class FinishTaskHandler implements IResultHandler {

        @Override
        public void onHandle(LinkedBlockingQueue<ITask> queue) {
            // TODO Auto-generated method stub

        }

    }

}

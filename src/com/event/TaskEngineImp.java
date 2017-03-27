
package com.event;

import com.event.ITask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEngineImp {
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);

    public TaskEngineImp() {

    }

    public void run() {
        TaskContext context = new TaskContext();
        LinkedBlockingQueue<ITask> taskQueue = context.getTaskList();
        TaskHandler handler = new TaskHandler(context);
        while (mIsRunning.get()) {
            ITask task = null;
            try {
                task = taskQueue.take();
            } catch (InterruptedException e) {
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

    public interface IResultHandler {
        public void onHandle(TaskContext context);
    }

    public class TaskHandler {
        IResultHandler mHandler = null;
        TaskContext mContext = null;

        public TaskHandler(TaskContext context) {
            mContext = context;
        }

        public void set(IResultHandler handler) {
            mHandler = handler;
        }

        public void handle() {
            mHandler.onHandle(mContext);
        }
    }

    public static class ChangeProcessHandler implements IResultHandler {

        @Override
        public void onHandle(TaskContext context) {

        }
    }

    public class RepeatProcessHandler implements IResultHandler {

        @Override
        public void onHandle(TaskContext context) {
            ITask task = new CommonTask(context);
            context.addTask(task);
        }

    }

    public class DefaultTaskHandler implements IResultHandler {

        @Override
        public void onHandle(TaskContext context) {
            // TODO Auto-generated method stub

        }

    }

    public class FinishTaskHandler implements IResultHandler {

        @Override
        public void onHandle(TaskContext context) {
            // TODO Auto-generated method stub

        }

    }

}

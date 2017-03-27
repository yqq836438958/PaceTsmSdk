
package com.event;

import com.event.ITask;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEngineImp implements ITaskEngine {
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    private TaskParam mTaskParam = null;
    private ProcessIdRouter mProcessIdRouter = null;

    // 入口
    public TaskEngineImp(TaskParam param) {
        mTaskParam = param;
        mProcessIdRouter = new ProcessIdRouter();
    }

    public void run() {
        TaskContext context = new TaskContext(mTaskParam);
        LinkedBlockingQueue<ITask> taskQueue = new LinkedBlockingQueue<ITask>();
        TaskHandler handler = new TaskHandler(context);
        ITask tmpTask = null;
        while (mIsRunning.get()) {
            ITask task = null;
            task = taskQueue.poll(); // TODO if it should be blocked
            if (task == null) {
                mIsRunning.set(false);
                break;
            }
            TaskResult result = task.exec();
            tmpTask = handler.handle(result.getResultHandler());
            if (tmpTask != null) {
                taskQueue.add(tmpTask);
            }
        }
    }

    public interface IResultHandler {
        public ITask onHandle(TaskContext context, ProcessIdRouter processIdRouter);
    }

    public class TaskHandler {
        TaskContext mContext = null;

        public TaskHandler(TaskContext context) {
            mContext = context;
        }

        public ITask handle(IResultHandler handler) {
            return handler.onHandle(mContext, mProcessIdRouter);
        }
    }

    // 执行下一个Processor
    public static class NextProcessHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context, ProcessIdRouter processIdRouter) {
            int pid = processIdRouter.nextPid();
            return new CommonTask(context, pid);
        }
    }

    // 重复上次的Processor
    public static class RepeatProcessHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context, ProcessIdRouter processIdRouter) {
            int pid = processIdRouter.repeatPid();
            return new CommonTask(context, pid);
        }

    }

    // 由任务队列去决定是否需要终止Processor
    public static class DefaultTaskHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context, ProcessIdRouter processIdRouter) {
            int pid = processIdRouter.nextPid();
            return new CommonTask(context, pid);
        }

    }

    // 无条件终止Processor
    public static class EndTaskHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context, ProcessIdRouter processIdRouter) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Override
    public TaskResult start() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}

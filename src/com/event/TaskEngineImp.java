
package com.event;

import com.event.ITask;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEngineImp implements ITaskEngine, Callable<TaskResult> {
    // TODO 需改造成一个自动运行的引擎，可以随时接收参数
    private static ITaskEngine sInstance = null;
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    // 每添加一个TaskInput，意味着，又增加了一个业务需求
    private LinkedBlockingQueue<TaskInput> mTaskInputQueue = new LinkedBlockingQueue<TaskInput>();

    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private ConcurrentHashMap<Long, Future<TaskResult>> mFutueMap = new ConcurrentHashMap<Long, Future<TaskResult>>();
    private static Object slockObj = new Object();

    public static ITaskEngine get() {
        if (sInstance == null) {
            synchronized (TaskEngineImp.class) {
                if (sInstance == null) {
                    sInstance = new TaskEngineImp();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void create() {
    }

    @Override
    public void destroy() {
        mIsRunning.set(false);
        mTaskInputQueue.clear();
    }

    @Override
    public long input(TaskInput param) {
        long curTime = System.currentTimeMillis();
        mTaskInputQueue.add(param);
        mFutueMap.put(curTime, mExecutor.submit(this));
        return 0;
    }

    @Override
    public TaskOutPut getOutput(long reqId) {
        Future<TaskResult> future = mFutueMap.get(reqId);
        TaskResult result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return TaskOutPut.unwrap(result);
    }

    @Override
    public TaskResult call() throws Exception {
        synchronized (slockObj) {
            TaskResult result = null;
            TaskInput input = mTaskInputQueue.take();// TODO
            TaskContext context = new TaskContext(input);
            TaskHandler handler = new TaskHandler(context);
            LinkedBlockingQueue<ITask> taskQueue = initTaskQueue(context);

            ITask tmpTask = taskQueue.poll();
            while (tmpTask != null) {
                // TODO if it should be blocked
                result = tmpTask.exec();
                tmpTask = handler.handle(result.getResultHandler());
                if (tmpTask != null) {
                    taskQueue.add(tmpTask);
                }
                tmpTask = taskQueue.poll();
            }
            return result;
        }
    }

    private LinkedBlockingQueue<ITask> initTaskQueue(TaskContext context) {
        LinkedBlockingQueue<ITask> taskQueue = new LinkedBlockingQueue<ITask>();
        // TODO 添加初始task
        ITask firstTask = new CommonTask(context, context.getRouter().firstPid());
        taskQueue.add(firstTask);
        return taskQueue;
    }

    public interface IResultHandler {
        public ITask onHandle(TaskContext context);
    }

    public class TaskHandler {
        TaskContext mContext = null;

        public TaskHandler(TaskContext context) {
            mContext = context;
        }

        public ITask handle(IResultHandler handler) {
            return handler.onHandle(mContext);
        }
    }

    // 执行下一个Processor
    public static class NextProcessHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context) {
            int pid = context.getRouter().nextPid();
            return new CommonTask(context, pid);
        }
    }

    // 重复上次的Processor
    public static class RepeatProcessHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context) {
            int pid = context.getRouter().repeatPid();
            return new CommonTask(context, pid);
        }

    }

    // 由任务队列去决定是否需要终止Processor
    public static class DefaultTaskHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context) {
            int pid = context.getRouter().nextPid();
            return new CommonTask(context, pid);
        }

    }

    // 无条件终止Processor
    public static class EndTaskHandler implements IResultHandler {

        @Override
        public ITask onHandle(TaskContext context) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}

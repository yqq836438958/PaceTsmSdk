
package com.pace.event;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskEngineImp implements ITaskEngine, Callable<TaskEvent> {
    // TODO 需改造成一个自动运行的引擎，可以随时接收参数
    private static ITaskEngine sInstance = null;
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    // 每添加一个TaskEventSource，意味着，又增加了一个业务需求
    private LinkedBlockingQueue<TaskEventSource> mTaskEventSourceQueue = new LinkedBlockingQueue<TaskEventSource>();
    private LinkedBlockingQueue<PidRouter> mPidRouterQueue = new LinkedBlockingQueue<PidRouter>();
    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private ConcurrentHashMap<Long, Future<TaskEvent>> mFutueMap = new ConcurrentHashMap<Long, Future<TaskEvent>>();
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
        mTaskEventSourceQueue.clear();
    }

    @Override
    public long addTask(TaskEventSource param, PidRouter router) {
        long curTime = System.currentTimeMillis();
        mTaskEventSourceQueue.add(param);
        mPidRouterQueue.add(router);
        mFutueMap.put(curTime, mExecutor.submit(this));
        return 0;
    }

    @Override
    public void cancelTask(long reqId) {
        Future<TaskEvent> future = mFutueMap.get(reqId);
        if (future == null) {
            return;
        }
        future.cancel(true);
    }

    @Override
    public TaskOutPut getOutput(long reqId) {
        Future<TaskEvent> future = mFutueMap.get(reqId);
        TaskEvent result = null;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return TaskOutPut.unwrap(result);
    }

    @Override
    public TaskEvent call() throws Exception {
        synchronized (slockObj) {
            TaskEvent result = null;
            TaskEventSource input = mTaskEventSourceQueue.take();// TODO
            PidRouter router = mPidRouterQueue.take();
            TaskContext context = new TaskContext(input, router);
            TaskHandler handler = new TaskHandler(context);
            LinkedBlockingQueue<ITask> taskQueue = initTaskQueue(context);

            ITask tmpTask = taskQueue.poll();
            while (tmpTask != null) {
                // TODO if it should be blocked
                result = tmpTask.exec();
                tmpTask = handler.call(result.getResultHandler(), tmpTask);
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
        ITask firstTask = new CommonTask(context, context.getPidRouter().first());
        taskQueue.add(firstTask);
        return taskQueue;
    }

    public interface IEventHandler {
        public ITask onHandle(TaskContext context, ITask lastTask);
    }

    class TaskHandler {
        TaskContext mContext = null;

        public TaskHandler(TaskContext context) {
            mContext = context;
        }

        public ITask call(IEventHandler handler, ITask lastTask) {
            return handler.onHandle(mContext, lastTask);
        }
    }

    // 执行下一个Processor
    public static class NextProcessHandler implements IEventHandler {

        @Override
        public ITask onHandle(TaskContext context, ITask lastTask) {
            int pid = context.getPidRouter().next();
            return new CommonTask(context, pid);
        }
    }

    // 重复上次的Processor
    public static class RepeatProcessHandler implements IEventHandler {

        @Override
        public ITask onHandle(TaskContext context, ITask lastTask) {
            int pid = context.getPidRouter().repeat();
            return new CommonTask(context, pid);
        }

    }

    // 由任务队列去决定是否需要终止Processor
    public static class DefaultProcessHandler implements IEventHandler {

        @Override
        public ITask onHandle(TaskContext context, ITask lastTask) {
            int pid = context.getPidRouter().next();
            return new CommonTask(context, pid);
        }

    }

    // 无条件终止Processor
    public static class EndProcessHandler implements IEventHandler {

        @Override
        public ITask onHandle(TaskContext context, ITask lastTask) {
            lastTask.clear();
            return null;
        }

    }

}

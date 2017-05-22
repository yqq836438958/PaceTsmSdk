
package com.pace.event;

import com.pace.common.RET;
import com.pace.processor.Dispatcher;
import com.pace.processor.Dispatcher.IBusinessType;
import com.pace.processor.bean.ParamBean;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventEngineImp implements IEventEngine, Callable<RET> {
    // TODO 需改造成一个自动运行的引擎，可以随时接收参数
    private static IEventEngine sInstance = null;
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    private LinkedBlockingQueue<Event> mTaskEventSourceQueue = new LinkedBlockingQueue<Event>();
    private ExecutorService mExecutor = Executors.newCachedThreadPool();
    private ConcurrentHashMap<Long, Future<RET>> mFutueMap = new ConcurrentHashMap<Long, Future<RET>>();
    private static Object slockObj = new Object();

    public static IEventEngine get() {
        if (sInstance == null) {
            synchronized (EventEngineImp.class) {
                if (sInstance == null) {
                    sInstance = new EventEngineImp();
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
    public void cancelTask(long reqId) {
        Future<RET> future = mFutueMap.get(reqId);
        if (future == null) {
            return;
        }
        future.cancel(true);
    }

    @Override
    public RET call() throws Exception {
        Event event = mTaskEventSourceQueue.poll();
        RET ret = RET.empty();
        if (event != null) {
            synchronized (slockObj) {
                ret = Dispatcher.getInstance().invoke(event.getMsg(), event.getEventType());
            }
        }
        return ret;
    }

    @Override
    public long offer(ParamBean msg, IBusinessType type) {
        long curTime = System.currentTimeMillis();
        mTaskEventSourceQueue.add(new Event(type, msg));
        mFutueMap.put(curTime, mExecutor.submit(this));
        return curTime;
    }

    @Override
    public RET take(long reqId) {
        Future<RET> future = mFutueMap.get(reqId);
        RET result = RET.empty();
        try {
            result = (future != null ? future.get() : result);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}


package com.pace.processor;

import android.util.SparseArray;

import com.pace.constants.CommonConstants;
import com.pace.event.IBaseProcessor;
import com.pace.event.TaskInput;
import com.pace.processor.apdu.CardCplc;
import com.pace.processor.apdu.CardListQuery;
import com.pace.processor.apdu.CardNetBusiness;
import com.pace.processor.apdu.CardQuery;
import com.pace.processor.apdu.CardSwitch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ProcessorPools {
    private static ProcessorPools sInstance = null;
    private SparseArray<Class<?>> mProcessors = null;

    public static ProcessorPools get() {
        if (sInstance == null) {
            synchronized (ProcessorPools.class) {
                sInstance = new ProcessorPools();
            }
        }
        return sInstance;
    }

    private ProcessorPools() {
        mProcessors.put(CommonConstants.TASK_CARD_CPLC, CardCplc.class);
        mProcessors.put(CommonConstants.TASK_CARD_LIST, CardListQuery.class);
        mProcessors.put(CommonConstants.TASK_CARD_SWITCH, CardSwitch.class);
        mProcessors.put(CommonConstants.TASK_CARD_NET_BUSINESS, CardNetBusiness.class);
        mProcessors.put(CommonConstants.TASK_CARD_QUERY, CardQuery.class);
    }

    public IBaseProcessor getProcess(int processId) {
        IBaseProcessor invoker = null;
        Class<?> clz = mProcessors.get(processId);
        if (clz == null) {
            return invoker;
        }
        // TODO 参数的传递
        try {
            Constructor<?> c = clz.getConstructor(TaskInput.class);
            invoker = (IBaseProcessor) c.newInstance(null);// TODO
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return invoker;
    }

    public IBaseProcessor getProcess(TaskInput param) {
        int processId = param.pid();
        return getProcess(processId);
    }
}

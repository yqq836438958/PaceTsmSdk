
package com.pace.processor;

import android.util.SparseArray;

import com.event.IBaseProcessor;
import com.event.TaskInput;
import com.pace.processor.apdu.CardCplc;
import com.pace.processor.apdu.CardListQuery;
import com.pace.processor.apdu.CardNetBusiness;
import com.pace.processor.apdu.CardSwitch;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ProcessorFactory {
    private static ProcessorFactory sInstance = null;
    private SparseArray<Class<?>> mProcessors = null;

    public static ProcessorFactory get() {
        if (sInstance == null) {
            synchronized (ProcessorFactory.class) {
                sInstance = new ProcessorFactory();
            }
        }
        return sInstance;
    }

    private ProcessorFactory() {
        // apdu process
        mProcessors.put(1, CardCplc.class);
        mProcessors.put(2, CardListQuery.class);
        mProcessors.put(3, CardSwitch.class);
        mProcessors.put(4, CardNetBusiness.class);

        // wup process add here
        // ...
    }

    public IBaseProcessor getProcess(int processId) {
        IBaseProcessor invoker = null;
        Class<?> clz = mProcessors.get(processId);
        if (clz == null) {
            return invoker;
        }

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

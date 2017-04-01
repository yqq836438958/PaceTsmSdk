
package com.pace.processor;

import com.pace.constants.CommonConstants;
import com.pace.event.IBaseProcessor;
import com.pace.event.TaskEventSource;
import com.pace.processor.apdu.CardCplc;
import com.pace.processor.apdu.CardListQuery;
import com.pace.processor.apdu.CardNetBusiness;
import com.pace.processor.apdu.CardQuery;
import com.pace.processor.apdu.CardSwitch;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessorPools {
    private static ProcessorPools sInstance = null;
    private ConcurrentHashMap<Integer, IBaseProcessor> mProcessorMap = null;

    public static ProcessorPools get() {
        if (sInstance == null) {
            synchronized (ProcessorPools.class) {
                sInstance = new ProcessorPools();
            }
        }
        return sInstance;
    }

    private ProcessorPools() {
        mProcessorMap = new ConcurrentHashMap<Integer, IBaseProcessor>();
    }

    public IBaseProcessor getProcess(TaskEventSource param, int customPid) {
        IBaseProcessor invoker = mProcessorMap.get(customPid);
        if (invoker != null) {
            return invoker;
        }
        switch (customPid) {
            case CommonConstants.TASK_CARD_CPLC:
                invoker = new CardCplc(param);
                break;
            case CommonConstants.TASK_CARD_LIST:
                invoker = new CardListQuery(param);
                break;
            case CommonConstants.TASK_CARD_QUERY:
                invoker = new CardQuery(param);
                break;
            case CommonConstants.TASK_CARD_SWITCH:
                invoker = new CardSwitch(param);
                break;
            case CommonConstants.TASK_CARD_NET_BUSINESS:
                invoker = new CardNetBusiness(param);
                break;
            default:
                break;
        }
        mProcessorMap.put(customPid, invoker);
        return invoker;
    }

    public IBaseProcessor getProcess(TaskEventSource param) {
        return getProcess(param, param.targetId());
    }

    public void clear() {
        // 清除已开辟的processors
        mProcessorMap.clear();
    }
}

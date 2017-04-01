
package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.provider.ApduProvider.NetApduStrategy;
import com.pace.processor.provider.IApduProvider;

import java.util.List;

public class CardNetBusiness extends ApduProcessor {
    private IApduProvider mApduProvider = null;

    // 初始化的数据？？TODO
    public CardNetBusiness(TaskEventSource param) {
        super(param,TASK_CARDNETBUSINESS);
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        // 是否需要进行操作
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        return mApduProvider.call(new NetApduStrategy(input));
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

}

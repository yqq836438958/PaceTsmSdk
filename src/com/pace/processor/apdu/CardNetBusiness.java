
package com.pace.processor.apdu;

import com.pace.event.TaskInput;
import com.pace.event.TaskResult;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.provider.ApduProvider.NetApduStrategy;
import com.pace.processor.provider.IApduProvider;

import java.util.List;

public class CardNetBusiness extends ApduProcessor {
    private IApduProvider mApduProvider = null;

    // 初始化的数据？？TODO
    public CardNetBusiness(TaskInput param) {
        super();
    }

    @Override
    protected TaskResult prepare(TaskResult input) {
        // 是否需要进行操作
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskResult input) {
        return mApduProvider.call(new NetApduStrategy(input));
    }

    @Override
    protected TaskResult handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

}


package com.pace.processor.apdu;

import com.event.TaskInput;
import com.event.TaskResult;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.provider.IApduProvider;

import java.util.List;

public class CardNetBusiness extends ApduProcessor {
    private IApduProvider mApduProvider = null;

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
        return mApduProvider.provide(input);
    }

    @Override
    protected TaskResult handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

}

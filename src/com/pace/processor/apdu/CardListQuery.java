
package com.pace.processor.apdu;

import com.pace.event.TaskInput;
import com.pace.event.TaskResult;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;

import java.util.List;

// 发起者，或者是终结者？
public class CardListQuery extends ApduProcessor {

    CardListQuery(TaskInput param) {
        super();
    }

    @Override
    protected TaskResult prepare(TaskResult input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskResult input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TaskResult handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

}

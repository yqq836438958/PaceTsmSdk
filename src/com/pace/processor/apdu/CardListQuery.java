
package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;

import java.util.List;

// 发起者，或者是终结者？
public class CardListQuery extends ApduProcessor {

    public CardListQuery(TaskEventSource param) {
        super(param, TASK_CARDLISTQUERY);
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

}

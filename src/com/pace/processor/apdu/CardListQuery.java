
package com.pace.processor.apdu;

import com.event.TaskParam;
import com.event.TaskResult;
import com.pace.processor.ApduProcess;

import java.util.List;

// 发起者，或者是终结者？
public class CardListQuery extends ApduProcess {

    CardListQuery(TaskParam param) {
        super();
    }

    @Override
    protected List<String> onApduReq(TaskResult input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TaskResult onApduRsp(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TaskResult getCacheApdu(TaskResult input) {
        // TODO Auto-generated method stub
        return null;
    }

}

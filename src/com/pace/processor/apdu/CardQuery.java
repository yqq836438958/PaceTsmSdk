
package com.pace.processor.apdu;

import com.event.TaskParam;
import com.event.TaskResult;
import com.pace.processor.ApduProcess;

import java.util.List;

public class CardQuery extends ApduProcess {

    public CardQuery(TaskParam param) {
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

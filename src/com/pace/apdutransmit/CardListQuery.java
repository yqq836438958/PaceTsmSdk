
package com.pace.apdutransmit;

import com.event.TaskResult;
import com.pace.api.IApduChannel;
import com.pace.events.ApduProcess;

import java.util.List;

public class CardListQuery extends ApduProcess {

    CardListQuery(IApduChannel channel) {
        super(channel);
        // TODO Auto-generated constructor stub
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

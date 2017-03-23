
package com.pace.apdutransmit;

import com.event.TaskResult;
import com.pace.api.IApduChannel;
import com.pace.events.ApduProcess;

import java.util.List;

public class CardSwitch extends ApduProcess {

    CardSwitch(IApduChannel channel) {
        super(channel);
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

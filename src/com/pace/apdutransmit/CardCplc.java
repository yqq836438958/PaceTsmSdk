
package com.pace.apdutransmit;

import com.event.TaskResult;
import com.pace.api.IApduChannel;
import com.pace.constants.ApduConstants;
import com.pace.events.ApduProcess;

import java.util.ArrayList;
import java.util.List;

public class CardCplc extends ApduProcess {

    CardCplc(IApduChannel channel) {
        super(channel);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected List<String> onApduReq(TaskResult input) {
        List<String> list = new ArrayList<String>();
        list.add(ApduConstants.CPLC);
        return list;
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

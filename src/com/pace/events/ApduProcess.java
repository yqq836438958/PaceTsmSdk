
package com.pace.events;

import com.event.IBaseProcess;
import com.event.TaskResult;
import com.pace.api.IApduChannel;

import java.util.List;

public abstract class ApduProcess implements IBaseProcess {
    public static final int TASK_ISSUECARD = 0;
    public static final int TASK_CARDTOPUP = 1;
    public static final int TASK_CARDQUERY = 2;
    public static final int TASK_CARDSWITCH = 3;
    public static final int TASK_CARDLISTQUERY = 4;
    public static final int TASK_CARDCPLC = 5;
    private IApduChannel mChannel = null;

    protected ApduProcess(IApduChannel channel) {
        mChannel = channel;
    }

    @Override
    public TaskResult process(TaskResult input) {
        TaskResult result = getCacheApdu(input);
        if (result != null) {
            return result;
        }
        List<String> apdus = onApduReq(input);
        List<String> apdursp = null;
        if (mChannel != null) {
            apdursp = mChannel.transmit(apdus);
        }
        return onApduRsp(apdursp);
    }

    protected abstract TaskResult getCacheApdu(TaskResult input);

    protected abstract List<String> onApduReq(TaskResult input);

    protected abstract TaskResult onApduRsp(List<String> apdus);
}

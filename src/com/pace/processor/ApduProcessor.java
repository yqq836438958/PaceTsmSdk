
package com.pace.processor;

import com.event.IBaseProcessor;
import com.event.TaskResult;
import com.pace.api.IApduChannel;
import com.pace.processor.apdu.ApduChannelFactory;

import java.util.List;

public abstract class ApduProcessor implements IBaseProcessor {
    public static final int TASK_ISSUECARD = 0;
    public static final int TASK_CARDTOPUP = 1;
    public static final int TASK_CARDQUERY = 2;
    public static final int TASK_CARDSWITCH = 3;
    public static final int TASK_CARDLISTQUERY = 4;
    public static final int TASK_CARDCPLC = 5;
    private IApduChannel mChannel = null;

    protected ApduProcessor() {
        mChannel = ApduChannelFactory.get().getChannel();
    }

    @Override
    public TaskResult process(TaskResult input) {
        TaskResult result = prepare(input);
        if (result != null) {
            return result;
        }
        APDU apdu = provideAPDU(input);
        List<String> apdursp = null;
        if (mChannel != null) {
            apdursp = mChannel.transmit(apdu.req());
        }
        return handleAPDU(apdursp);
    }

    protected abstract TaskResult prepare(TaskResult input);

    protected abstract APDU provideAPDU(TaskResult input);

    protected abstract TaskResult handleAPDU(List<String> apdus);

}

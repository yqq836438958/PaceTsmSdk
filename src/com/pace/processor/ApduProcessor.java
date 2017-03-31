
package com.pace.processor;

import com.pace.event.IBaseProcessor;
import com.pace.event.TaskResult;
import com.pace.api.IApduChannel;
import com.pace.processor.provider.ApduProvider;
import com.pace.processor.provider.IApduProvider;

import java.util.List;

public abstract class ApduProcessor implements IBaseProcessor {
    public static final int TASK_ISSUECARD = 0;
    public static final int TASK_CARDTOPUP = 1;
    public static final int TASK_CARDQUERY = 2;
    public static final int TASK_CARDSWITCH = 3;
    public static final int TASK_CARDLISTQUERY = 4;
    public static final int TASK_CARDCPLC = 5;
    private IApduChannel mChannel = null;
    protected IApduProvider mApduProvider = null;

    protected ApduProcessor() {
        mChannel = ApduChannelFactory.get().getChannel();
        mApduProvider = new ApduProvider();
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
            apdursp = mChannel.transmit(apdu.getData());
        }
        result = handleAPDU(apdursp);
        if (result.hasComplete()) {
        }
        return result;
    }

    protected abstract TaskResult prepare(TaskResult input);

    protected abstract APDU provideAPDU(TaskResult input);

    protected abstract TaskResult handleAPDU(List<String> apdus);

    // protected abstract void free();

}

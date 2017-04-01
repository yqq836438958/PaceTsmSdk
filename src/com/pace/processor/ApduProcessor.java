
package com.pace.processor;

import com.pace.event.IBaseProcessor;
import com.pace.event.TaskEvent;
import com.pace.event.TaskEventSource;
import com.pace.api.IApduChannel;
import com.pace.processor.provider.ApduProvider;
import com.pace.processor.provider.IApduProvider;

import java.util.List;

public abstract class ApduProcessor implements IBaseProcessor {
    private IApduChannel mChannel = null;
    protected IApduProvider mApduProvider = null;
    protected TaskEventSource mEventSource = null;
    private int mCurPid = 0;

    protected ApduProcessor(TaskEventSource param, int pid) {
        mEventSource = param;
        mChannel = ApduChannelFactory.get().getChannel();
        mApduProvider = new ApduProvider();
        mCurPid = pid;
    }

    @Override
    public TaskEvent process(TaskEvent input) {
        TaskEvent result = prepare(input);
        if (result != null) {
            return result;
        }
        APDU apdu = provideAPDU(input);
        List<String> apdursp = null;
        if (mChannel != null) {
            apdursp = mChannel.transmit(apdu.getData());
        }
        result = handleAPDU(apdursp);
        return result;
    }

    protected abstract TaskEvent prepare(TaskEvent input);

    protected abstract APDU provideAPDU(TaskEvent input);

    protected abstract TaskEvent handleAPDU(List<String> apdus);

    public final int getCurPid() {
        return mCurPid;
    }

    protected final TaskEvent retrieveTaskEvent(Object obj) {
        if (mEventSource.targetId() == getCurPid()) {
            return TaskEvent.end(obj);
        }
        return TaskEvent.next(obj);
    }
}

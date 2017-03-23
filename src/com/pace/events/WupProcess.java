
package com.pace.events;

import com.event.IBaseProcess;
import com.event.TaskResult;

import java.util.List;

public abstract class WupProcess implements IBaseProcess {
    @Override
    public TaskResult process(TaskResult input) {
        return TaskResult.emptyResult();
    }

    protected abstract Byte[] requestWup(Byte[] packet);

    protected abstract Byte[] prepareWup(List<String> apdus);

    protected abstract List<String> onParseWupData(Byte[] data);
}

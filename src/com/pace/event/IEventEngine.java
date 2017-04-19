
package com.pace.event;

import com.pace.common.RET;
import com.pace.processor.Dispatcher.IBusinessType;

public interface IEventEngine {
    public long offer(String msg, IBusinessType type);

    public void cancelTask(long reqId);

    public void create();

    public void destroy();

    public RET take(long reqId);
}

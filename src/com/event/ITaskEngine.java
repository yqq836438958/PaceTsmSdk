
package com.event;

public interface ITaskEngine {
    public long input(TaskInput msg);

    public void create();

    public void destroy();

    public TaskOutPut getOutput(long reqId);
}

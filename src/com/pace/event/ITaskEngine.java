
package com.pace.event;

public interface ITaskEngine {
    public long addTask(TaskInput msg);

    public void cancelTask(long reqId);

    public void create();

    public void destroy();

    public TaskOutPut getOutput(long reqId);
}

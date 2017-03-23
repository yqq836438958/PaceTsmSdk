
package com.event;

public interface ITask {
    public void setProcess(IBaseProcess process);

    public TaskResult exec();
}

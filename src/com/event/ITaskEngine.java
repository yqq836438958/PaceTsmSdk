
package com.event;

public interface ITaskEngine {

    public void regist(ITask task);

    public void prepare();

    public TaskResult start();

}


package com.pace.processor.provider;

import com.event.TaskResult;
import com.pace.processor.APDU;

public interface IApduProvider {
    public APDU provide(TaskResult input);
}

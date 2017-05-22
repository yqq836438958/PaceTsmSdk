
package com.pace.event;

import com.pace.processor.Dispatcher.IBusinessType;
import com.pace.processor.bean.ParamBean;

public class Event {
    private IBusinessType type;
    private ParamBean sMsg;

    Event(IBusinessType t, ParamBean str) {
        type = t;
        sMsg = str;
    }

    ParamBean getMsg() {
        return sMsg;
    }

    IBusinessType getEventType() {
        return type;
    }
}

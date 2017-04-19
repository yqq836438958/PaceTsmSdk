
package com.pace.event;

import com.pace.processor.Dispatcher.IBusinessType;

public class Event {
    private IBusinessType type;
    private String sMsg;

    Event(IBusinessType t, String str) {
        type = t;
        sMsg = str;
    }

    String getMsg() {
        return sMsg;
    }

    IBusinessType getEventType() {
        return type;
    }
}

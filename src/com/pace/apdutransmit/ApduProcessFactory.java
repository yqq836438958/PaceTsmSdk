
package com.pace.apdutransmit;

import com.pace.api.IApduChannel;
import com.pace.events.ApduProcess;
import com.pace.tsm.TsmSdkParam;

public class ApduProcessFactory {
    public static ApduProcessFactory get() {
        return null;
    }

    public ApduProcess newProcess(TsmSdkParam param) {
        ApduProcess invoker = null;
        IApduChannel channel = ApduChannelFactory.get().getChannel();
        switch (param.getOperation()) {
            case ApduProcess.TASK_CARDLISTQUERY:
                invoker = new CardQuery(channel, param);
                break;
            case ApduProcess.TASK_ISSUECARD:
            case ApduProcess.TASK_CARDTOPUP:
                invoker = new CardTransmit(channel, param);
                break;
            default:
                break;
        }
        return invoker;
    }
}


package com.pace.processor.provider;

import com.event.TaskResult;
import com.pace.processor.APDU;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;
import com.qq.taf.jce.JceStruct;

public class NetApduProvider implements IApduProvider {
    @Override
    public APDU provide(TaskResult input) {
        TsmTosService apduGet = new GetTsmApdu();
        JceStruct rsp = apduGet.invokeSync();
        return apduGet.parseRspApdu(rsp);
    }

}

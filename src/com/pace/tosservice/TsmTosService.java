
package com.pace.tosservice;

import com.pace.httpserver.BaseTosService;
import com.pace.processor.APDU;
import com.qq.taf.jce.JceStruct;

public abstract class TsmTosService extends BaseTosService {

    @Override
    public String getFunctionName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOperType() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected JceStruct getJceHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    protected abstract APDU parseRspApdu(JceStruct rsp);

    public final APDU requestApdu() {
        JceStruct rsp = this.invokeSync();
        return parseRspApdu(rsp);
    }
}

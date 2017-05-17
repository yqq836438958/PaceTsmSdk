
package com.pace.processor.internal.provider;

import com.pace.processor.APDU;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.processor.internal.base.NetApduData;
import com.pace.processor.internal.base.NetApduStore;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;

public class NetApduStrategy implements IApduProviderStrategy {
    private int mType = 0;

    // 请求参数
    public NetApduStrategy(Object input) {
    }

    @Override
    public APDU provide() {

        // TODO 解析出来
        // NetApduRsp lastNetApduRsp =
        NetApduData lastApduRsp = NetApduStore.get().getLastData(mType);
        TsmTosService getApdu = new GetTsmApdu();
        APDU apdu = getApdu.requestApdu();
        NetApduStore.get().add(mType, null); // TODO
        return apdu;
    }

}

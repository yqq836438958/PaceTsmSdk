
package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;

import java.util.List;

public class CardNetBusiness extends CardBaseBusiness {

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        return nextProvide(null);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new NetApduStrategy(input));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        // TODO Auto-generated method stub
        return nextProvide(null);
    }

    @Override
    protected RET finalResult() {
        // TODO Auto-generated method stub
        return RET.suc("");
    }

    public static class NetApduStrategy implements IApduProviderStrategy {

        // 请求参数
        public NetApduStrategy(Object input) {
        }

        @Override
        public APDU provide() {

            // TODO 解析出来
            TsmTosService getApdu = new GetTsmApdu();
            return getApdu.requestApdu();
        }

    }

}

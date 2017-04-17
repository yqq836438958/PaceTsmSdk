
package com.pace.processor.internal;

import com.pace.processor.APDU;
import com.pace.processor.IApduProvider.IApduProviderStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;

import java.util.List;

public class CardNetBusiness extends CardBaseBusiness {

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        return new ApduResult(APDU_STEP.FINAL, true);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new NetApduStrategy(input));
        return new ApduResult(APDU_STEP.APDU_PROVIDE, apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String finalResult() {
        // TODO Auto-generated method stub
        return null;
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

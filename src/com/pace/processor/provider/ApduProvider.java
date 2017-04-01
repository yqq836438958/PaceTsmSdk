
package com.pace.processor.provider;

import com.pace.event.TaskEvent;
import com.pace.log.LogPrint;
import com.pace.constants.ApduConstants;
import com.pace.processor.APDU;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;

import java.util.ArrayList;
import java.util.List;

public class ApduProvider implements IApduProvider {

    public APDU cardQuery(String tag) {
        return null;
    }

    public static abstract class ApduProviderStrategy implements IApduProviderStrategy {
        protected TaskEvent mInput = null;

        public ApduProviderStrategy(TaskEvent input) {
            mInput = input;
        }
    }

    public static class CplcStrategy extends ApduProviderStrategy {

        public CplcStrategy(TaskEvent input) {
            super(input);
        }

        @Override
        public APDU provide() {
            List<String> list = new ArrayList<String>();
            list.add(ApduConstants.CPLC);
            return new APDU(list);
        }

    }

    public static class CardTagQueryStrategy extends ApduProviderStrategy {

        public CardTagQueryStrategy(TaskEvent input) {
            super(input);
        }

        @Override
        public APDU provide() {
            return null;
        }

    }

    public static class NetApduStrategy extends ApduProviderStrategy {

        public NetApduStrategy(TaskEvent input) {
            super(input);
        }

        @Override
        public APDU provide() {
            Object req = mInput.getOject();
            if (req instanceof String) {
                // 说明这里是最开始的请求
            } else if (req instanceof APDU) {
                // 说明是从handleAPDU过来的数据
            } else {
                LogPrint.e("what? eror");
            }
            // 解析出来，放到数据里面去GetTsmApdu去填充
            TsmTosService getApdu = new GetTsmApdu();
            return getApdu.requestApdu();
        }

    }

    @Override
    public APDU call(IApduProviderStrategy strategy) {
        return strategy.provide();
    }
}

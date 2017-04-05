
package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.log.LogPrint;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;

import java.util.List;

public class CardNetBusiness extends ApduProcessor {

    // 初始化的数据？？TODO
    public CardNetBusiness(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_NET_BUSINESS);
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        // 是否需要进行操作
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        return mApduProvider.call(new NetApduStrategy(input));
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        // TODO Auto-generated method stub
        return null;
    }

    public static class NetApduStrategy implements IApduProviderStrategy {
        private TaskEvent mInput = null;

        public NetApduStrategy(TaskEvent input) {
            mInput = input;
        }

        @Override
        public APDU provide() {

            // TODO 解析出来
            TsmTosService getApdu = new GetTsmApdu();
            return getApdu.requestApdu();
        }

    }
}


package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.NetApduStore;
import com.pace.processor.internal.provider.NetApduStrategy;

import java.util.List;

public class CardNetBusiness extends CardBaseBusiness {
    private int mType = 0;
    private String mOutput = "";

    @Override
    protected ApduResult<Boolean> onPrepare(String sourceInput) {
        return nextProvide(null);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new NetApduStrategy(input));
        if (apdu.getRet() == 0) {
            return nextTransmit(apdu);
        }
        return nextFinal(null);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        NetApduStore.get().append(mType, apduList);
        return nextProvide(new APDU(apduList));
    }

    @Override
    protected RET finalResult() {
        return RET.suc(mOutput);
    }

}

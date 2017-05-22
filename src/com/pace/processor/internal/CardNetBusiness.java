
package com.pace.processor.internal;

import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.NetApduStore;
import com.pace.processor.internal.provider.NetApduStrategy;
import com.pace.processor.internal.state.ProcessContext;

import java.util.List;

public class CardNetBusiness extends CardBaseProcess {
    private int mType = 0;
    private String mOutput = "";

    // @Override
    // protected ApduResult onPrepare(String sourceInput) {
    // return nextProvide(null);
    // }
    //
    // @Override
    // protected ApduResult onApduProvide(Object input) {
    // APDU apdu = mApduProvider.call(new NetApduStrategy(input));
    // if (apdu.getRet() == 0) {
    // return nextTransmit(apdu);
    // }
    // return nextFinal(RET.suc(mOutput));
    // }
    //
    // @Override
    // protected ApduResult onApduConsume(List<String> apduList) {
    // NetApduStore.get().append(mType, apduList);
    // return nextProvide(new APDU(apduList));
    // }

    @Override
    protected int onPrepare(ProcessContext context) {
        return 0;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        NetApduStore.get().append(mType, apduList);
        return 0;
    }

}

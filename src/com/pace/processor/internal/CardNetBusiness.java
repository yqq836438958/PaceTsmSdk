
package com.pace.processor.internal;

import com.pace.processor.APDU;

public class CardNetBusiness extends CardBaseBusiness {

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        // TODO Auto-generated method stub
        return new ApduResult(APDU_STEP.FINAL, true);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ApduResult onApduConsume(APDU apdu) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ApduResult<String> finalResult() {
        // TODO Auto-generated method stub
        return null;
    }

}

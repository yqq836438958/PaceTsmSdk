
package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.provider.CplcStrategy;
import com.pace.util.TextUtils;

import java.util.List;

public class CardCplc extends CardBaseBusiness {
    private String mCplc = null;

    public CardCplc() {
    }

    @Override
    protected ApduResult<Boolean> onPrepare(String sourceInput) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return nextProvide(Boolean.FALSE);
        }
        mCplc = cacheCplc;
        return nextFinal(null);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new CplcStrategy());
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return nextFinal(null);
    }

    @Override
    protected RET finalResult() {
        return RET.suc(mCplc);
    }

}

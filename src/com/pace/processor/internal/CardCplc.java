
package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.provider.CplcStrategy;
import com.pace.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CardCplc extends CardBaseBusiness {
    private String mCplc = null;

    public CardCplc() {
    }

    @Override
    protected ApduResult<APDU> onPrepare(String sourceInput) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return nextProvide(null);
        }
        mCplc = cacheCplc;
        return nextFinal(null);
    }

    @Override
    protected ApduResult onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new CplcStrategy());
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult onApduConsume(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return nextFinal(RET.suc(mCplc));
    }

    private List getList() {
        if (true) {
            return new ArrayList<String>();
        } else {
            return new ArrayList<Integer>();
        }
    }
}


package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.constants.ApduConstants;
import com.pace.processor.APDU;
import com.pace.processor.IApduProvider.IApduProviderStrategy;
import com.pace.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CardCplc extends CardBaseBusiness {
    private String mCplc = null;

    public CardCplc() {
    }

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return new ApduResult<Boolean>(APDU_STEP.APDU_PROVIDE, Boolean.FALSE);
        }
        mCplc = cacheCplc;
        return nextFinal(mCplc);
        // return new ApduResult<Boolean>(APDU_STEP.FINAL, Boolean.TRUE);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new CplcStrategy());
        return new ApduResult<APDU>(APDU_STEP.APDU_TRANSIMT, apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return new ApduResult<APDU>(APDU_STEP.FINAL, null);
    }

    @Override
    protected String finalResult() {
        return mCplc;
    }

    class CplcStrategy implements IApduProviderStrategy {

        public CplcStrategy() {
        }

        @Override
        public APDU provide() {
            List<String> list = new ArrayList<String>();
            list.add(ApduConstants.CPLC);
            return new APDU(list);
        }

    }
}


package com.pace.processor.apdu;

import com.pace.cache.TsmCache;
import com.pace.constants.ApduConstants;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEventSource;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;
import com.pace.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CardCplc extends ApduProcessor {
    private String mCplc = null;

    public CardCplc(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_CPLC);
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return null;
        }
        mCplc = cacheCplc;
        return retrieveTaskEvent(cacheCplc);
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        return mApduProvider.call(new CplcStrategy());
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return retrieveTaskEvent(mCplc);
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

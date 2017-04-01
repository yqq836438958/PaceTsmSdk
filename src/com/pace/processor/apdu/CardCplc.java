
package com.pace.processor.apdu;

import com.pace.cache.TsmCache;
import com.pace.event.TaskEventSource;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.provider.ApduProvider.CplcStrategy;
import com.pace.util.TextUtils;

import java.util.List;

public class CardCplc extends ApduProcessor {
    private String mCplc = null;

    public CardCplc(TaskEventSource param) {
        super(param, TASK_CARDCPLC);
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return null;
        }
        return retrieveTaskEvent(cacheCplc);
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        return mApduProvider.call(new CplcStrategy(input));
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return retrieveTaskEvent(mCplc);
    }

}

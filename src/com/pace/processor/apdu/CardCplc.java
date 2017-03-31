
package com.pace.processor.apdu;

import com.pace.cache.TsmCache;
import com.pace.event.TaskInput;
import com.pace.event.TaskResult;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.provider.ApduProvider.CplcStrategy;
import com.pace.util.TextUtils;

import java.util.List;

public class CardCplc extends ApduProcessor {
    private String mCplc = null;

    CardCplc(TaskInput param) {
        super();
    }

    @Override
    protected TaskResult prepare(TaskResult input) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return null;
        }
        return TaskResult.newResult(TaskResult.TASK_FINISH, cacheCplc);
    }

    @Override
    protected APDU provideAPDU(TaskResult input) {
        return mApduProvider.call(new CplcStrategy(input));
    }

    @Override
    protected TaskResult handleAPDU(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return TaskResult.newResult(TaskResult.TASK_FINISH, mCplc);
    }

}

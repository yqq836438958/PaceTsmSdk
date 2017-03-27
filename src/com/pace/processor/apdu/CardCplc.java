
package com.pace.processor.apdu;

import com.event.TaskParam;
import com.event.TaskResult;
import com.pace.cache.TsmCache;
import com.pace.constants.ApduConstants;
import com.pace.processor.ApduProcess;
import com.pace.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class CardCplc extends ApduProcess {
    private String mCplc = null;

    CardCplc(TaskParam param) {
        super();
    }

    @Override
    protected List<String> onApduReq(TaskResult input) {
        List<String> list = new ArrayList<String>();
        list.add(ApduConstants.CPLC);
        return list;
    }

    @Override
    protected TaskResult onApduRsp(List<String> apdus) {
        mCplc = apdus.get(0);
        TsmCache.saveCplc(mCplc);
        return TaskResult.newResult(TaskResult.TASK_FINISH, mCplc);
    }

    // 如果运行在手表/手环端，是可以做缓存的；
    // 但是若运行在手机端，那么不做缓存
    @Override
    protected TaskResult getCacheApdu(TaskResult input) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (TextUtils.isEmpty(cacheCplc)) {
            return null;
        }
        return TaskResult.newResult(TaskResult.TASK_FINISH, cacheCplc);
    }

}

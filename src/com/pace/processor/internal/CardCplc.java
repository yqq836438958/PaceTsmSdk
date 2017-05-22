
package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.common.ErrCode;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.provider.CplcStrategy;
import com.pace.processor.internal.state.ProcessContext;
import com.pace.util.TextUtils;

import java.util.List;

public class CardCplc extends CardBaseProcess {
    private String mCplc = null;

    @Override
    protected int onPrepare(ProcessContext context) {
        String cacheCplc = TextUtils.isEmpty(mCplc) ? TsmCache.getCplc() : mCplc;
        if (!TextUtils.isEmpty(cacheCplc)) {
            context.setParam(mCplc);
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        APDU apdu = mApduProvider.call(new CplcStrategy());
        if (apdu == null) {
            return ErrCode.ERR_NET_APDU_NULL;
        }
        context.setParam(apdu);
        return RET.RET_NEXT;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        mCplc = apduList.get(0);
        TsmCache.saveCplc(mCplc);
        return RET.RET_NEXT;
    }

}

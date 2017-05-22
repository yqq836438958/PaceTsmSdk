
package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.processor.internal.provider.ListStrategy;
import com.pace.processor.internal.state.ProcessContext;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;
import com.pace.util.TextUtils;

import org.json.JSONArray;

import java.util.List;

// cardlistquery,from net or local
public class CardListQuery extends CardBaseProcess {
    // input:{"instance_aid":"xxx","invoke":0}
    // crs aid
    private boolean mIsLocalInvoke = false;
    private JSONArray mOutpArray = new JSONArray();
    private String mCRSAid = "";

    public CardListQuery(String crsAid) {
        mCRSAid = crsAid;
    }

    public CardListQuery() {
        // TODO parse bLocalInvoke from param
    }

    @Override
    protected int onPrepare(ProcessContext context) {
        String list = TsmCache.getCardList();
        if (!TextUtils.isEmpty(list)) {
            context.setParam(list);
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        APDU apdu = mApduProvider.call(new ListStrategy(null, ""));// TODO
        if (apdu == null || apdu.isEmpty()) {
            return -1;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        if (mIsLocalInvoke) {
            // 本地访问的时候需要额外解析

        }
        return RET.RET_NEXT;
    }

}

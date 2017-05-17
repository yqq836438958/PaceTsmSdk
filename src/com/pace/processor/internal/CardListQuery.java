
package com.pace.processor.internal;

import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.processor.internal.provider.ListStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;
import com.pace.util.TextUtils;

import org.json.JSONArray;

import java.util.List;

// cardlistquery,from net or local
public class CardListQuery extends CardBaseBusiness {
    // input:{"instance_aid":"xxx","invoke":0}
    // crs aid
    private boolean mIsLocalInvoke = false;
    private JSONArray mOutpArray = new JSONArray();

    public CardListQuery() {
        // TODO parse bLocalInvoke from param
    }

    @Override
    protected ApduResult onPrepare(String sourceInput) {
        String list = TsmCache.getCardList();
        if (!TextUtils.isEmpty(list)) {
            return nextFinal(RET.suc(list));
        }
        return nextProvide(null);
    }

    @Override
    protected ApduResult onApduProvide(Object input) {
        APDU apdu = mApduProvider.call(new ListStrategy(input, ""));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult onApduConsume(List<String> apduList) {
        if (mIsLocalInvoke) {
            // 本地访问的时候需要额外解析

        }
        return nextProvide(new APDU(apduList));
    }

    // @Override
    // protected RET finalResult() {
    // return RET.suc(mOutpArray.toString());
    // }
}

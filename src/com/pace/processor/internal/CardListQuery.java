
package com.pace.processor.internal;

import com.pace.event.TaskEventSource;
import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
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
    protected TaskEvent onPrepare(TaskEvent input) {
        String list = TsmCache.getCardList();
        if (!TextUtils.isEmpty(list)) {
            return nextFinal(list);
        }
        return null;
    }

    @Override
    protected APDU onProvide(TaskEvent input) {
        return mApduProvider.call(new ListStrategy(input, ""));// TODO
    }

    @Override
    protected TaskEvent onPost(List<String> apdus) {
        // TODO Auto-generated method stub
        // TODO
        // 处理apdu rsp
        if (mIsLocalInvoke) {
            // 本地访问的时候需要额外解析

        }
        return retrieveTaskEvent(mOutpArray.toString());
    }

    public class ListStrategy implements IApduProviderStrategy {
        private String mAidCRS;

        public ListStrategy(Object input, String crsAid) {
            mAidCRS = crsAid;
        }

        @Override
        public APDU provide() {
            if (!TextUtils.isEmpty(mAidCRS)) {
                return new APDU(ApduHelper.lsCRS(mAidCRS));
            }

            // 解析出来，放到数据里面去GetTsmApdu去填充
            TsmTosService getApdu = new GetTsmApdu();
            return getApdu.requestApdu();
        }

    }

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String finalResult() {
        // TODO Auto-generated method stub
        return null;
    }
}

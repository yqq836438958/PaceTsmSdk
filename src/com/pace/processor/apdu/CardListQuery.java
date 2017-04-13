
package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;
import com.pace.util.TextUtils;

import org.json.JSONArray;

import java.util.List;

// cardlistquery,from net or local
public class CardListQuery extends ApduProcessor {
    // input:{"instance_aid":"xxx","invoke":0}
    // crs aid
    private boolean mIsLocalInvoke = false;
    private JSONArray mOutpArray = new JSONArray();

    public CardListQuery(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_LIST);
        // TODO parse bLocalInvoke from param
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        String list = TsmCache.getCardList();
        if (!TextUtils.isEmpty(list)) {
            return retrieveTaskEvent(list);
        }
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        return mApduProvider.call(new ListStrategy(input, ""));// TODO
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
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
}

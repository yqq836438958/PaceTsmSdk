
package com.pace.processor.internal;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.common.ErrCode;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardListQueryBean;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.internal.provider.CardSwitchStrategy;
import com.pace.processor.internal.state.ProcessContext;

import java.util.List;

public class CardSwitch extends CardBaseProcess {
    private String mTargetOperateAid = null;
    private List<CardListQueryBean> mCardListBeans = null;
    private int mCardListIndex = -1;

    @Override
    protected int onPrepare(ProcessContext context) {
        ParamBean bean = context.getSource();
        mTargetOperateAid = bean.getData();
        //
        mCardListBeans = getDataFromCache(TsmCache.get().getCardList());
        // mCardListBeans = GsonUtil.<CardListQueryBean> parseJsonArrayWithGson(
        // TsmCache.get().getCardList(),
        // CardListQueryBean.class);
        if (mCardListBeans == null || mCardListBeans.size() <= 0) {
            return ErrCode.ERR_CARDLIST_NULL;
        }
        if (isTargetAidActivited()) {
            return RET.RET_OVER;
        }
        // TODO 重新排序列表
        return RET.RET_NEXT;
    }

    private List<CardListQueryBean> getDataFromCache(String cache) {
        Gson gson = new Gson();
        return gson.fromJson(cache, new TypeToken<List<CardListQueryBean>>() {
        }.getType());
    }

    private boolean isTargetAidActivited() {
        for (CardListQueryBean bean : mCardListBeans) {
            if (mTargetOperateAid.equalsIgnoreCase(bean.getInstance_id())) {
                if (bean.getActivite_status() == CardListQueryBean.ACT_TRUE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        mCardListIndex++;
        if (mCardListIndex >= mCardListBeans.size()) {
            return RET.RET_OVER;
        }
        CardListQueryBean targetBean = mCardListBeans.get(mCardListIndex);
        boolean isTargetAid = (targetBean.getInstance_id() == mTargetOperateAid);
        if (!isTargetAid && targetBean.getActivite_status() == CardListQueryBean.ACT_FALSE) {
            return RET.RET_REFRESH;
        }
        boolean toAct = (targetBean.getActivite_status() == CardListQueryBean.ACT_FALSE);
        APDU apdu = mApduProvider.call(new CardSwitchStrategy(targetBean, toAct));
        context.setParam(apdu);
        return RET.RET_NEXT;

    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        if (!ApduHelper.isResponseSuc(apduList)) {
            return ErrCode.ERR_RSP_APDU_FAIL;
        }
        CardListQueryBean targetBean = mCardListBeans.get(mCardListIndex);
        int preStatus = targetBean.getActivite_status();
        targetBean.setActivte_status(preStatus == CardListQueryBean.ACT_FALSE
                ? CardListQueryBean.ACT_TRUE : CardListQueryBean.ACT_FALSE);
        TsmCache.get().saveCardList(new Gson().toJson(mCardListBeans));
        if (mCardListIndex == mCardListBeans.size() - 1) {
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }
}

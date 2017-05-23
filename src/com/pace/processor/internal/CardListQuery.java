
package com.pace.processor.internal;

import com.google.gson.Gson;
import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardListQueryBean;
import com.pace.processor.internal.provider.ListStrategy;
import com.pace.processor.internal.state.ProcessContext;
import com.pace.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

// cardlistquery,from net or local
public class CardListQuery extends CardBaseProcess {
    // input:{"instance_aid":"xxx","invoke":0}
    // crs aid
    private String mCRSAid = "";
    private boolean mIsLocalInvoke = false;
    private List<CardListQueryBean> mCardListQueryBeans = new ArrayList<CardListQueryBean>();

    public CardListQuery(String crsAid) {
        mCRSAid = crsAid;
        mIsLocalInvoke = !TextUtils.isEmpty(mCRSAid);
    }

    public CardListQuery() {
        mIsLocalInvoke = false;
    }

    @Override
    protected int onPrepare(ProcessContext context) {
        String list = TsmCache.get().getCardList();
        if (!TextUtils.isEmpty(list)) {
            context.setParam(list);
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        if (mIsLocalInvoke) {
            context.setParam(ApduHelper.listCRSApp());
            return RET.RET_NEXT;
        }
        APDU apdu = mApduProvider.call(new ListStrategy(null, ""));// TODO
        if (apdu == null || apdu.isEmpty()) {
            return -1;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        if (mIsLocalInvoke) {
            // // 本地访问的时候需要额外解析
            filterAidAct(apduList);
            context.setParam(new Gson().toJson(mCardListQueryBeans));
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    private void filterAidAct(List<String> apduList) {
        ICardPluginService service = PluginManager.getInstance().getService();
        List<String> supportList = service.getSupportAidList();
        if (supportList == null || supportList.size() <= 0) {
            return;
        }
        String rsp = apduList.get(0);
        for (String aidConfig : supportList) {
            if (!ApduHelper.isAidExisit(rsp, aidConfig)) {
                continue;
            }
            int stat = ApduHelper.isAppActived(rsp, aidConfig);
            mCardListQueryBeans
                    .add(new CardListQueryBean(aidConfig, CardListQueryBean.INS_PERSON, stat));
        }
    }
}

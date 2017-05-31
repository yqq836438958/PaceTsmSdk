
package com.pace.processor.internal;

import com.google.gson.Gson;
import com.pace.cache.TsmCache;
import com.pace.common.ApduHelper;
import com.pace.common.ErrCode;
import com.pace.common.RET;
import com.pace.plugin.PluginManager;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardListQueryBean;
import com.pace.processor.internal.provider.ListStrategy;
import com.pace.processor.internal.state.ProcessContext;
import com.pace.tsm.plugin.ICardPluginService;
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
    private int mLocalInvokeCnt = -1;

    public CardListQuery(String crsAid) {
        mCRSAid = crsAid;
        mIsLocalInvoke = !TextUtils.isEmpty(mCRSAid);
    }

    public CardListQuery() {
        mIsLocalInvoke = false;
    }

    // TODO 本地访问需要自己去判断！！
    @Override
    protected int onPrepare(ProcessContext context) {
        if (mIsLocalInvoke) {
            return RET.RET_NEXT;
        }
        String list = TsmCache.get().getCardList();
        if (!TextUtils.isEmpty(list)) {
            context.setOutPut(list);
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        if (mIsLocalInvoke) {
            mLocalInvokeCnt++;
            List<String> cmds = new ArrayList<String>();
            if (mLocalInvokeCnt == 0) {
                cmds.add(ApduHelper.selectAid(mCRSAid));
            }
            cmds.add(ApduHelper.listCRSApp(mLocalInvokeCnt));
            context.setParam(new APDU(cmds));
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
        int iRet = RET.RET_NEXT;
        if (mIsLocalInvoke) {
            // // 本地访问的时候需要额外解析
            String result = apduList.get(apduList.size() - 1);
            if (result.endsWith("6310")) {
                filterAidAct(result);
                iRet = RET.RET_NEXT;
            } else if (result.endsWith("9000")) {
                filterAidAct(result);
                String cardList = new Gson().toJson(mCardListQueryBeans);
                context.setOutPut(cardList);
                saveCardListResult(cardList);
                iRet = RET.RET_OVER;
            } else {
                iRet = ErrCode.ERR_UNKOWN_ERR;
            }
            return iRet;
        }
        return iRet;
    }

    private void filterAidAct(String rsp) {
        ICardPluginService service = PluginManager.getInstance().getService();
        List<String> supportList = service.getSupportAidList();
        if (supportList == null || supportList.size() <= 0) {
            return;
        }
        for (String aidConfig : supportList) {
            if (!ApduHelper.isAidExisit(rsp, aidConfig)) {
                continue;
            }
            int stat = ApduHelper.isAppActived(rsp, aidConfig);
            mCardListQueryBeans
                    .add(new CardListQueryBean(aidConfig, CardListQueryBean.INS_PERSON, stat));
        }
    }

    private void saveCardListResult(String list) {
        TsmCache.get().saveCardList(list);
    }
}

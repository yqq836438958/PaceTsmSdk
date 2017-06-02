
package com.pace.processor.internal;

import com.pace.plugin.PluginManager;

import android.text.TextUtils;

import com.pace.common.RET;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardQueryBean;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.internal.provider.CardTagQueryStrategy;
import com.pace.processor.internal.state.ProcessContext;
import com.pace.tsm.plugin.ICardPluginService;
import com.pace.tsm.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardQuery extends CardBaseProcess {
    private List<String> mTagList = null;
    private int mTagIndex = -1;
    private JSONObject mOutPut = null;
    private String mAid = "";

    public CardQuery() {
        // TODO
        mOutPut = new JSONObject();
    }

    @Override
    protected int onPrepare(ProcessContext context) {
        // {"instance_id":"xxx","tag":"xxx,xx"}
        ParamBean input = context.getSource();
        CardQueryBean bean = GsonUtil.parseJsonWithGson(input.getData(), CardQueryBean.class);
        mAid = bean.getInstance_id();
        String tmp = bean.getTag();
        if (TextUtils.isEmpty(tmp)) {
            initNoFilterTagList();
            return 0;
        }
        String[] tmps = tmp.split(",");
        if (tmps == null || tmps.length <= 0) {
            initNoFilterTagList();
            return 0;
        }
        mTagList = new ArrayList<String>();
        for (String tag : tmps) {
            mTagList.add(tag);
        }
        return 0;
    }

    private void initNoFilterTagList() {
        mTagList = Arrays.asList("amount", "card_num", "date");
    }

    @Override
    protected int onProvider(ProcessContext context) {
        mTagIndex++;
        if (mTagIndex >= mTagList.size()) {
            genOutput(context);
            return RET.RET_OVER;
        }
        String tag = mTagList.get(mTagIndex);
        APDU apdu = mApduProvider.call(new CardTagQueryStrategy(mAid, tag));
        context.setParam(apdu);
        return 0;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        String tag = mTagList.get(mTagIndex);
        ICardPluginService service = PluginManager.getInstance().getService();
        String parseData = service.parseDetailRsp(mAid, tag, apduList);
        try {
            mOutPut.put(tag, parseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mTagIndex == mTagList.size() - 1) {
            genOutput(context);
            return RET.RET_OVER;
        }
        return RET.RET_NEXT;
    }

    private void genOutput(ProcessContext context) {
        try {
            mOutPut.put(CommonConstants.INSTANCE_ID, mAid);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.setOutPut(mOutPut.toString());
    }
}

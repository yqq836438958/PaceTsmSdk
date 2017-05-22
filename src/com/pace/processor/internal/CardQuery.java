
package com.pace.processor.internal;

import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.common.GsonUtil;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardQueryBean;
import com.pace.processor.bean.ParamBean;
import com.pace.processor.internal.base.CardTagElement;
import com.pace.processor.internal.provider.CardTagQueryStrategy;
import com.pace.processor.internal.state.ProcessContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardQuery extends CardBaseProcess {
    private ConcurrentLinkedQueue<String> mCardTagElements = new ConcurrentLinkedQueue<String>();
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
        String[] tmps = tmp.split(",");
        for (String tag : tmps) {
            mCardTagElements.add(tag);
        }
        return 0;
    }

    @Override
    protected int onProvider(ProcessContext context) {
        String tag = mCardTagElements.peek();
        APDU apdu = mApduProvider.call(new CardTagQueryStrategy(mAid, tag));
        context.setParam(apdu);
        return 0;
    }

    @Override
    protected int onPostHandle(ProcessContext context, List<String> apduList) {
        String element = mCardTagElements.poll();
        if (element == null) {
            context.setParam(mOutPut.toString());
            return RET.RET_OVER;
        }
        ICardPluginService service = PluginManager.getInstance().getService();
        String parseData = service.parseDetailRsp(mAid, element, apduList);
        try {
            mOutPut.put(element, parseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return RET.RET_NEXT;
    }

}

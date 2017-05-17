
package com.pace.processor.internal;

import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.CardTagElement;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.processor.internal.provider.CardTagQueryStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardQuery extends CardBaseBusiness {
    private ConcurrentLinkedQueue<CardTagElement> mCardTagElements = null;
    private JSONObject mOutPut = null;

    public CardQuery() {
        // TODO
        mOutPut = new JSONObject();
    }

    @Override
    protected ApduResult onPrepare(String sourceInput) {
        return nextProvide(null);
    }

    @Override
    protected ApduResult onApduProvide(Object input) {
        CardTagElement element = mCardTagElements.peek();
        APDU apdu = mApduProvider.call(new CardTagQueryStrategy(element));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult onApduConsume(List<String> apduList) {
        CardTagElement element = mCardTagElements.poll();
        if (element == null) {
            return nextFinal(RET.suc(mOutPut.toString()));
        }
        ICardPluginService service = PluginManager.getInstance().getService();
        String parseData = service.parseDetailRsp(element.aid, element.tag, apduList);
        try {
            mOutPut.put(element.tag, parseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nextProvide(null);
    }

}


package com.pace.processor.internal;

import com.pace.event.TaskEventSource;
import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.common.RET;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;

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

    public static class CardTagQueryStrategy implements IApduProviderStrategy {
        private CardTagElement mCardTagElement = null;

        public CardTagQueryStrategy(CardTagElement input) {
            mCardTagElement = input;
        }

        @Override
        public APDU provide() {
            ICardPluginService service = PluginManager.getInstance().getService();
            if (service == null) {
                return null;
            }
            return new APDU(
                    service.fetchDetailReq(mCardTagElement.aid, mCardTagElement.tag));
        }

    }

    class CardTagElement implements Serializable {
        private static final long serialVersionUID = 1L;
        private String aid;
        private String tag;
    }

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        return nextProvide(null);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        CardTagElement element = mCardTagElements.peek();
        APDU apdu = mApduProvider.call(new CardTagQueryStrategy(element));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        CardTagElement element = mCardTagElements.poll();
        if (element == null) {
            return nextFinal(mOutPut.toString());
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

    @Override
    protected RET finalResult() {
        return RET.suc(mOutPut.toString());
    }
}

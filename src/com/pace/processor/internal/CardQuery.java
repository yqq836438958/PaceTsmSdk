
package com.pace.processor.internal;

import com.pace.event.TaskEventSource;
import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.IApduProvider.IApduProviderStrategy;

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
    protected TaskEvent onPrepare(TaskEvent input) {
        return null;
    }

    @Override
    protected APDU onProvide(TaskEvent input) {
        CardTagElement element = mCardTagElements.peek();
        return mApduProvider.call(new CardTagQueryStrategy(element));
    }

    @Override
    protected TaskEvent onPost(List<String> apdus) {
        CardTagElement element = mCardTagElements.poll();
        if (element == null) {
            return retrieveTaskEvent(mOutPut.toString());
        }
        ICardPluginService service = PluginManager.getInstance().getService();
        if (service == null) {
            return TaskEvent.error();
        }
        String parseData = service.parseDetailRsp(element.aid, element.tag, apdus);
        try {
            mOutPut.put(element.tag, parseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return repeatTaskEvent(mOutPut.toString());
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
        return new ApduResult<Boolean>(APDU_STEP.APDU_PROVIDE, Boolean.FALSE);
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        CardTagElement element = mCardTagElements.peek();
        APDU apdu = mApduProvider.call(new CardTagQueryStrategy(element));
        return new ApduResult<APDU>(APDU_STEP.APDU_TRANSIMT, apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        CardTagElement element = mCardTagElements.poll();
        if (element == null) {
            return retrieveTaskEvent(mOutPut.toString());
        }
        ICardPluginService service = PluginManager.getInstance().getService();
        String parseData = service.parseDetailRsp(element.aid, element.tag, apdus);
        try {
            mOutPut.put(element.tag, parseData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String finalResult() {
        return mOutPut.toString();
    }
}


package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardQuery extends ApduProcessor {
    private ConcurrentLinkedQueue<CardTagElement> mCardTagElements = null;
    private JSONObject mOutPut = null;

    public CardQuery(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_QUERY);
        // TODO
        mOutPut = new JSONObject();
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        CardTagElement element = mCardTagElements.peek();
        return mApduProvider.call(new CardTagQueryStrategy(element));
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
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
        return repeatTaskEvent(null);
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
}

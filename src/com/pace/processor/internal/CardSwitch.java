
package com.pace.processor.internal;

import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.SwitchCardElement;
import com.pace.processor.internal.provider.CardSwitchStrategy;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardSwitch extends CardBaseBusiness {
    private ConcurrentLinkedQueue<SwitchCardElement> mAidQueue = null;
    private JSONArray mOriginArray = new JSONArray();
    private JSONArray mTargetArray = new JSONArray();
    private String mTargetOperateAid = null;

    @Override
    protected ApduResult<APDU> onPrepare(String sourceInput) {
        SwitchCardElement element = mAidQueue.peek();
        if (!mTargetOperateAid.equalsIgnoreCase(element.instance_id)
                && !element.needAct) {
            return null;
        }
        // TODO need check!
        return null;
    }

    @Override
    protected ApduResult onApduProvide(Object input) {
        SwitchCardElement element = mAidQueue.peek();
        APDU apdu = mApduProvider.call(new CardSwitchStrategy(element));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult onApduConsume(List<String> apduList) {
        SwitchCardElement element = mAidQueue.poll();
        SwitchCardElement newElement = element;
        if (element == null) {
            return nextFinal(RET.suc(""));
        }
        if (ApduHelper.isResponseSuc(apduList)) {
            // mTargetArray.put
            mTargetArray.put(element);
        }
        return nextProvide(null);
    }
}


package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;

import android.provider.ContactsContract.CommonDataKinds.Email;

import com.pace.common.ApduHelper;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardSwitch extends ApduProcessor {
    private ConcurrentLinkedQueue<SwitchCardElement> mAidQueue = null;
    private JSONArray mOriginArray = new JSONArray();
    private JSONArray mTargetArray = new JSONArray();
    private String mTargetOperateAid = null;

    // 需要依赖cardlistquery的结果
    public CardSwitch(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_SWITCH);
        // 按照 activite status排序 ,生成mAidQueue
        // [{"instance_id":"aaaaaaaaaa","activite_status":"2",..},{""}]
        // mTargetOperateAid = ??/
    }

    @Override
    protected TaskEvent onPrepare(TaskEvent input) {
        SwitchCardElement element = mAidQueue.peek();
        if (!mTargetOperateAid.equalsIgnoreCase(element.instance_id)
                && !element.needAct) {
            return repeatTaskEvent(element);
        }
        return null;
    }

    @Override
    protected APDU onProvide(TaskEvent input) {
        SwitchCardElement element = mAidQueue.peek();
        return mApduProvider.call(new CardSwitchStrategy(element));
    }

    @Override
    protected TaskEvent onPost(List<String> apdus) {
        SwitchCardElement element = mAidQueue.poll();
        SwitchCardElement newElement = element;
        if (element == null) {
            return retrieveTaskEvent(null);
        }
        if (ApduHelper.isResponseSuc(apdus)) {
            // mTargetArray.put
            mTargetArray.put(element);
        }
        return repeatTaskEvent(element);
    }

    class CardSwitchStrategy implements IApduProviderStrategy {
        SwitchCardElement mElement = null;

        public CardSwitchStrategy(SwitchCardElement input) {
            mElement = input;
        }

        @Override
        public APDU provide() {
            if (mElement == null) {
                return null;
            }
            String apdu = mElement.install_status > 0 ? ApduHelper.activeAid(mElement.instance_id)
                    : ApduHelper.disactiveAid(mElement.instance_id);
            return new APDU(apdu);
        }

    }

    class SwitchCardElement {
        String instance_id;
        boolean needAct;
    }
}

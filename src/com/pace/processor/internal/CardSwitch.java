
package com.pace.processor.internal;

import android.provider.ContactsContract.CommonDataKinds.Email;

import com.pace.common.ApduHelper;
import com.pace.common.RET;
import com.pace.constants.CommonConstants;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.ApduResult;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardSwitch extends CardBaseBusiness {
    private ConcurrentLinkedQueue<SwitchCardElement> mAidQueue = null;
    private JSONArray mOriginArray = new JSONArray();
    private JSONArray mTargetArray = new JSONArray();
    private String mTargetOperateAid = null;

    // 需要依赖cardlistquery的结果
    public CardSwitch() {
        // 按照 activite status排序 ,生成mAidQueue
        // [{"instance_id":"aaaaaaaaaa","activite_status":"2",..},{""}]
        // mTargetOperateAid = ??/
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
            // TODO need check!
            String apdu = mElement.needAct ? ApduHelper.activeAid(mElement.instance_id)
                    : ApduHelper.disactiveAid(mElement.instance_id);
            return new APDU(apdu);
        }

    }

    class SwitchCardElement {
        String instance_id;
        boolean needAct;
    }

    @Override
    protected ApduResult<Boolean> onCachPrepare() {
        SwitchCardElement element = mAidQueue.peek();
        if (!mTargetOperateAid.equalsIgnoreCase(element.instance_id)
                && !element.needAct) {
            return null;
        }
        // TODO need check!
        return null;
    }

    @Override
    protected ApduResult<APDU> onApduProvide(Object input) {
        SwitchCardElement element = mAidQueue.peek();
        APDU apdu = mApduProvider.call(new CardSwitchStrategy(element));
        return nextTransmit(apdu);
    }

    @Override
    protected ApduResult<APDU> onApduConsume(List<String> apduList) {
        SwitchCardElement element = mAidQueue.poll();
        SwitchCardElement newElement = element;
        if (element == null) {
            return nextFinal(null);
        }
        if (ApduHelper.isResponseSuc(apduList)) {
            // mTargetArray.put
            mTargetArray.put(element);
        }
        return nextProvide(null);
    }

    @Override
    protected RET finalResult() {
        // TODO Auto-generated method stub
        return RET.suc("");
    }
}

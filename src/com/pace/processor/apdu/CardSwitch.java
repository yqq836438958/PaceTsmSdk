
package com.pace.processor.apdu;

import com.pace.event.TaskEventSource;
import com.pace.common.ApduHelper;
import com.pace.constants.CommonConstants;
import com.pace.event.TaskEvent;
import com.pace.processor.APDU;
import com.pace.processor.ApduProcessor;
import com.pace.processor.IApduProvider.IApduProviderStrategy;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CardSwitch extends ApduProcessor {
    private ConcurrentLinkedDeque<SwitchCardElement> mAidQueue = null;

    // 需要依赖cardlistquery的结果
    public CardSwitch(TaskEventSource param) {
        super(param, CommonConstants.TASK_CARD_SWITCH);
        // 按照 activite status排序 ,生成mAidQueue
    }

    @Override
    protected TaskEvent prepare(TaskEvent input) {
        return null;
    }

    @Override
    protected APDU provideAPDU(TaskEvent input) {
        SwitchCardElement element = mAidQueue.peek();
        return mApduProvider.call(new CardSwitchStrategy(element));
    }

    @Override
    protected TaskEvent handleAPDU(List<String> apdus) {
        SwitchCardElement element = mAidQueue.poll();
        if (element == null) {
            return retrieveTaskEvent(null);
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
            String apdu = mElement.toAct ? ApduHelper.activeAid(mElement.aid)
                    : ApduHelper.disactiveAid(mElement.aid);
            return new APDU(apdu);
        }

    }

    class SwitchCardElement {
        String aid;
        boolean toAct;
    }
}

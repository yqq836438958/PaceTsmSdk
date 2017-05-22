
package com.pace.processor.internal.provider;

import com.pace.common.ApduHelper;
import com.pace.processor.APDU;
import com.pace.processor.bean.CardListQueryBean;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;

public class CardSwitchStrategy implements IApduProviderStrategy {
    CardListQueryBean mElement = null;
    private boolean mToAct = false;

    public CardSwitchStrategy(CardListQueryBean input, boolean toAct) {
        mElement = input;
        mToAct = toAct;
    }

    @Override
    public APDU provide() {
        if (mElement == null) {
            return null;
        }
        // TODO need check!
        String apdu = mToAct ? ApduHelper.activeAid(mElement.getInstance_id())
                : ApduHelper.disactiveAid(mElement.getInstance_id());
        return new APDU(apdu);
    }

}

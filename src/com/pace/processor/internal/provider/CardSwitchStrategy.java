
package com.pace.processor.internal.provider;

import com.pace.common.ApduHelper;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.processor.internal.base.SwitchCardElement;

public class CardSwitchStrategy implements IApduProviderStrategy {
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

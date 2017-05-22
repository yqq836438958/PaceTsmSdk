
package com.pace.processor.internal.provider;

import com.pace.plugin.ICardPluginService;
import com.pace.plugin.PluginManager;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.CardTagElement;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;

public class CardTagQueryStrategy implements IApduProviderStrategy {
    private String mAid = null;
    private String mTag = null;

    public CardTagQueryStrategy(String aid, String tag) {
        mAid = aid;
        mTag = tag;
    }

    @Override
    public APDU provide() {
        ICardPluginService service = PluginManager.getInstance().getService();
        if (service == null) {
            return null;
        }
        return new APDU(
                service.fetchDetailReq(mAid, mTag));
    }

}

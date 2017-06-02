
package com.pace.processor.internal.provider;

import com.pace.common.ApduHelper;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;
import com.pace.tosservice.GetTsmApdu;
import com.pace.tosservice.TsmTosService;
import com.pace.tsm.utils.ValueUtil;

public class ListStrategy implements IApduProviderStrategy {
    private String mAidCRS;

    public ListStrategy(Object input, String crsAid) {
        mAidCRS = crsAid;
    }

    @Override
    public APDU provide() {
        if (!ValueUtil.isEmpty(mAidCRS)) {
            return new APDU(ApduHelper.getCRSAppStat(mAidCRS));
        }

        // 解析出来，放到数据里面去GetTsmApdu去填充
        TsmTosService getApdu = new GetTsmApdu();
        return getApdu.requestApdu();
    }

}


package com.pace.processor.internal.provider;

import com.pace.constants.ApduConstants;
import com.pace.processor.APDU;
import com.pace.processor.internal.base.IApduProvider.IApduProviderStrategy;

import java.util.ArrayList;
import java.util.List;

public class CplcStrategy implements IApduProviderStrategy {

    public CplcStrategy() {
    }

    @Override
    public APDU provide() {
        List<String> list = new ArrayList<String>();
        list.add(ApduConstants.CPLC);
        return new APDU(list);
    }

}

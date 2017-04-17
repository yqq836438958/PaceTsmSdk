
package com.pace.processor.internal.base;

import com.pace.processor.APDU;

public class ApduProvider implements IApduProvider {

    @Override
    public APDU call(IApduProviderStrategy strategy) {
        return strategy.provide();
    }
}

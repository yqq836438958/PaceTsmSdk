
package com.pace.processor.provider;

import com.pace.processor.APDU;

public interface IApduProvider {
    public static interface IApduProviderStrategy {
        public APDU provide();
    }

    public APDU call(IApduProviderStrategy strategy);
}

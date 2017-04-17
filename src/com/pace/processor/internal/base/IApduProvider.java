
package com.pace.processor.internal.base;

import com.pace.processor.APDU;

public interface IApduProvider {
    public static interface IApduProviderStrategy {
        public APDU provide();
    }

    public APDU call(IApduProviderStrategy strategy);
}

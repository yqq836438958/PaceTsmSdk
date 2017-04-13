
package com.pace.processor.channel;

import com.pace.api.IApduChannel;

import java.util.List;

public class ApduChannel {
    private IApduChannel mChannel = null;
    private boolean isChannelOpen = false;
    private static ApduChannel sInstance = null;

    public static ApduChannel get() {
        if (sInstance == null)
            synchronized (ApduChannel.class) {
                if (sInstance == null) {
                    sInstance = new ApduChannel();
                }
            }
        return sInstance;
    }

    private ApduChannel() {

    }

    public final List<String> transmit(List<String> apdus) {
        if (!openChannel()) {
            return null;
        }
        return mChannel.transmit(apdus);
    }

    private boolean openChannel() {
        if (isChannelOpen) {
            return true;
        }
        if (mChannel != null) {
            return mChannel.open();
        }
        return false;
    }

    public final void close() {
        if (mChannel != null) {
            mChannel.close();
        }
    }

}

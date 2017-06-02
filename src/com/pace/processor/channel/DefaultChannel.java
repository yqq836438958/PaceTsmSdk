
package com.pace.processor.channel;

import android.content.Context;

import com.pace.api.IApduChannel;
import com.pace.tsm.se.OMAChannel;
import com.pace.tsm.utils.ByteUtil;

public class DefaultChannel implements IApduChannel {
    private OMAChannel mOmaChannel = null;

    public DefaultChannel(Context context) {
        mOmaChannel = new OMAChannel(context);
    }

    @Override
    public byte[] transmit(byte[] apdu) {
        return mOmaChannel.transmit(apdu);
    }

    @Override
    public boolean open() {
        byte[] rsp = mOmaChannel.selectAid(null);
        if (rsp != null && ByteUtil.toHexString(rsp).endsWith("9000")) {
            return true;
        }
        return false;
    }

    @Override
    public void close() {
        mOmaChannel.close();
    }

}

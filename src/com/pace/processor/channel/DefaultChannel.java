
package com.pace.processor.channel;

import android.content.Context;
import android.os.RemoteException;

import com.pace.tsm.se.OMAChannel;
import com.pace.tsm.service.IPaceApduChannel;
import com.pace.tsm.utils.ByteUtil;

public class DefaultChannel extends IPaceApduChannel.Stub {
    private OMAChannel mOmaChannel = null;

    public DefaultChannel(Context context) {
        mOmaChannel = new OMAChannel(context);
    }

    @Override
    public byte[] transmit(byte[] apdus) throws RemoteException {
        return mOmaChannel.transmit(apdus);
    }

    @Override
    public boolean open() throws RemoteException {
        byte[] rsp = mOmaChannel.selectAid(null);
        if (rsp != null && ByteUtil.toHexString(rsp).endsWith("9000")) {
            return true;
        }
        return false;
    }

    @Override
    public void close() throws RemoteException {
        mOmaChannel.close();
    }

}

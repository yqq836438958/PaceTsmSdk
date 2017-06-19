
package com.pace.processor.channel;

import android.content.Context;
import android.os.RemoteException;

import com.pace.tsm.BleLauncher;
import com.pace.tsm.service.IPaceApduChannel;

public class BleChannel extends IPaceApduChannel.Stub {

    public BleChannel(Context context) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public byte[] transmit(byte[] apdus) throws RemoteException {
        return BleLauncher.transmit(apdus);
    }

    @Override
    public boolean open() throws RemoteException {
        return BleLauncher.openChannel() == 0;
    }

    @Override
    public void close() throws RemoteException {
        BleLauncher.closeChannel();
    }

}

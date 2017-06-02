
package com.pace.tsm.se;

import android.content.Context;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.SEService.CallBack;
import org.simalliance.openmobileapi.Session;

import java.io.IOException;

public class OMAChannel implements CallBack {
    private SEService seService = null;
    private Session mSession = null;
    private Channel mChannel = null;

    public OMAChannel(Context context) {
        new SEService(context, this);
    }

    @Override
    public void serviceConnected(SEService arg0) {
        // TODO Auto-generated method stub
        seService = arg0;
        Reader[] readers = seService.getReaders();
        try {
            mSession = readers[0].openSession();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close() {
        mChannel.close();
    }

    public byte[] selectAid(byte[] apdu) {
        if (mSession != null) {
            try {
                mChannel = mSession.openBasicChannel(apdu);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mChannel != null) {
            return mChannel.getSelectResponse();
        }
        return null;
    }

    public byte[] transmit(byte[] apdu) {
        if (mChannel == null) {
            return null;
        }
        try {
            return mChannel.transmit(apdu);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

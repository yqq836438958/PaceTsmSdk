
package com.pace.httpserver;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author baodingzhou
 */

public abstract class ServerHandler implements IServerHandler,
        IServerHandlerListener {

    private static final String TAG = "ServerHandler";

    private static final int WUP_MOUDLE_ID = 11;

    private static volatile IServerHandler sInstance = null;

    private ArrayList<IServerHandlerListener> mListener = new ArrayList<IServerHandlerListener>();

    protected boolean mRequestEncrypt = false;

    public ServerHandler(Context context) {
        Log.d(TAG, "ServerHandler");
    }

    public static IServerHandler getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ServerHandler.class) {
                if (sInstance == null) {

                }
            }
        }

        return sInstance;
    }

    @Override
    public boolean registerServerHandlerListener(IServerHandlerListener listener) {
        synchronized (mListener) {
            if (!mListener.contains(listener)) {
                return mListener.add(listener);
            }

        }
        return false;
    }

    @Override
    public boolean unregisterServerHandlerListener(IServerHandlerListener listener) {
        synchronized (mListener) {
            return mListener.remove(listener);
        }
    }

    @Override
    public boolean onResponseSucceed(int reqID, int operType, byte[] response) {
        boolean handle = false;
        Iterator<IServerHandlerListener> iterator = null;
        IServerHandlerListener listener = null;
        synchronized (mListener) {
            iterator = mListener.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    listener = iterator.next();
                    handle = listener.onResponseSucceed(reqID, operType, response);
                    if (handle) {
                        break;
                    }
                }
            }
        }

        if (handle) {
            unregisterServerHandlerListener(listener);
        }

        return handle;
    }

    @Override
    public boolean onResponseFailed(int reqID, int operType, int errorCode, String description) {
        boolean handle = false;
        Iterator<IServerHandlerListener> iterator = null;
        IServerHandlerListener listener = null;
        synchronized (mListener) {
            iterator = mListener.iterator();
            if (iterator != null) {
                while (iterator.hasNext()) {
                    listener = iterator.next();
                    handle = listener.onResponseFailed(reqID, operType, errorCode, description);
                    if (handle) {
                        break;
                    }
                }
            }
        }

        if (handle) {
            unregisterServerHandlerListener(listener);
        }

        return handle;
    }

    @Override
    public void setRequestEncrypt(boolean encrypt) {
        mRequestEncrypt = encrypt;
    }

}


package com.pace.tsm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.pace.tsm.TsmApiAsync;
import com.pace.tsm.TsmApp;
import com.pace.tsm.TsmApiAsync.ITsmApiCallback;

public class TsmSdkAsyncService extends Service {
    private IPaceTsmSdkAsync.Stub mPaceTsmSdkAsync = new IPaceTsmSdkAsync.Stub() {

        @Override
        public void transQuerySe(String instance_id, IPaceTsmSdkCallBack callback)
                throws RemoteException {
            TsmApiAsync.cardTransaction(instance_id, getInnerApiCallback(callback));
        }

        @Override
        public void shutdown(IPaceTsmSdkCallBack callback) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void selectAid(String aid, IPaceTsmSdkCallBack callback) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void issueCard(String inputParam, IPaceTsmSdkCallBack callback)
                throws RemoteException {
            TsmApiAsync.cardIssue(inputParam, getInnerApiCallback(callback));
        }

        @Override
        public void initSe(IPaceTsmSdkCallBack callback) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void getCplc(IPaceTsmSdkCallBack callback) throws RemoteException {
            TsmApiAsync.cardCplc(getInnerApiCallback(callback));
        }

        @Override
        public void cardTopup(String inputParam, IPaceTsmSdkCallBack callback)
                throws RemoteException {
            TsmApiAsync.cardTopup(inputParam, getInnerApiCallback(callback));
        }

        @Override
        public void cardSwitch(String instance_id, IPaceTsmSdkCallBack callback)
                throws RemoteException {
            TsmApiAsync.cardSwitch(instance_id, getInnerApiCallback(callback));
        }

        @Override
        public void cardQuery(String inputParam, IPaceTsmSdkCallBack callback)
                throws RemoteException {
            TsmApiAsync.cardQuery(inputParam, getInnerApiCallback(callback));
        }

        @Override
        public void cardListQuery(IPaceTsmSdkCallBack callback) throws RemoteException {
            TsmApiAsync.cardListQuery(getInnerApiCallback(callback));
        }

        @Override
        public void regist(IPaceApduChannel channel) throws RemoteException {
             TsmApiAsync.regist(channel);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mPaceTsmSdkAsync;
    }

    private ITsmApiCallback getInnerApiCallback(final IPaceTsmSdkCallBack _callback) {
        ITsmApiCallback callback = new ITsmApiCallback() {

            @Override
            public void onCallback(int code, String result) {
                if (_callback != null) {
                    try {
                        _callback.onCallback(code, result);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        };
        return callback;
    }
}

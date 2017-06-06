
package com.pace.tsm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.pace.tsm.TsmApi;

public class TsmSdkService extends Service {
    private IPaceTsmSdk.Stub mPaceTsmSdk = new IPaceTsmSdk.Stub() {

        @Override
        public int transQuerySe(String instance_id, String[] outputParam) throws RemoteException {
            return TsmApi.cardTransaction(instance_id, outputParam);
        }

        @Override
        public byte[] selectAid(String aid, int[] resultCode) throws RemoteException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int issueCard(String inputParam, String[] outputParam) throws RemoteException {
            return TsmApi.cardIssue(inputParam, outputParam);
        }

        @Override
        public int initSe() throws RemoteException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getCplc(String[] cplc) throws RemoteException {
            return TsmApi.cardCplc(cplc);
        }

        @Override
        public void close() throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public int cardTopup(String inputParam, String[] outputParam) throws RemoteException {
            return TsmApi.cardTopup(inputParam, outputParam);
        }

        @Override
        public int cardSwitch(String instance_id) throws RemoteException {
            return TsmApi.cardSwitch(instance_id);
        }

        @Override
        public int cardQuery(String inputParam, String[] outputParam) throws RemoteException {
            return TsmApi.cardQuery(inputParam, outputParam);
        }

        @Override
        public int cardListQuery(String[] outputParam) throws RemoteException {
            return TsmApi.cardListQuery(outputParam);
        }

        @Override
        public void regist(IPaceApduChannel channel) throws RemoteException {
            TsmApi.regist(channel);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mPaceTsmSdk;
    }

}

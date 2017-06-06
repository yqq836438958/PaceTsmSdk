
package com.pace.processor.channel;

import android.content.Context;
import android.os.RemoteException;

import com.pace.common.ErrCode;
import com.pace.tsm.service.IPaceApduChannel;
import com.pace.tsm.utils.ByteUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ApduChannel {
    private IPaceApduChannel mChannel = null;
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

    public void setChannel(Context context, IPaceApduChannel channel) {
        if (channel == null) {
            channel = new DefaultChannel(context);
        }
        mChannel = channel;
    }

    private ApduChannel() {

    }

    public final int transmit(final List<String> input, final List<String> output) {
        int iRet = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        List<String> target = null;
        FutureTask<List<String>> transmitTask = new FutureTask<List<String>>(
                new Callable<List<String>>() {

                    @Override
                    public List<String> call() throws Exception {
                        if (!openChannel()) {
                            return null;
                        }
                        return transmitApduInternal(input);
                    }
                });
        executor.submit(transmitTask);
        try {
            target = transmitTask.get(5000, TimeUnit.MILLISECONDS); // 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
            if (output != null) {
                output.clear();
                output.addAll(target);
            }
        } catch (InterruptedException e) {
            iRet = ErrCode.ERR_THREAD_ERR;
            transmitTask.cancel(true);
        } catch (ExecutionException e) {
            iRet = ErrCode.ERR_THREAD_ERR;
            transmitTask.cancel(true);
        } catch (TimeoutException e) {
            iRet = ErrCode.ERR_APDU_REQ_TIMEOUT;
            transmitTask.cancel(true);
        } finally {
            executor.shutdown();
        }
        return iRet;
    }

    private List<String> transmitApduInternal(List<String> input) {
        List<String> rsp = new ArrayList<String>();
        byte[] apduSrc = null;
        byte[] apduRsp = null;
        for (String cmd : input) {
            apduSrc = ByteUtil.toByteArray(cmd);
            try {
                apduRsp = mChannel.transmit(apduSrc);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rsp.add(ByteUtil.toHexString(apduRsp));
        }
        return rsp;
    }

    private boolean openChannel() {
        if (isChannelOpen) {
            return true;
        }
        if (mChannel != null) {
            try {
                isChannelOpen = mChannel.open();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return isChannelOpen;
    }

    public final void close() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Void> closetTask = new FutureTask<Void>(
                new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        if (mChannel != null) {
                            mChannel.close();
                        }
                        return null;
                    }
                });
        executor.submit(closetTask);
        try {
            closetTask.get(5000, TimeUnit.MILLISECONDS); // 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
        } catch (InterruptedException e) {
            closetTask.cancel(true);
        } catch (ExecutionException e) {
            closetTask.cancel(true);
        } catch (TimeoutException e) {
            closetTask.cancel(true);
        } finally {
            executor.shutdown();
        }

    }

}

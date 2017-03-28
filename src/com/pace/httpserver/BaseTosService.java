
package com.pace.httpserver;

import com.common.SeqGenerator;
import com.log.LogPrint;
import com.pace.constants.CommonConstants;
import com.pace.tsm.TsmApi;
import com.qq.jce.wup.UniPacket;
import com.qq.taf.jce.JceStruct;

import java.lang.reflect.Field;

import qrom.component.wup.QRomWupDataBuilder;

/**
 * @author baodingzhou
 */

public abstract class BaseTosService
        implements IServerHandlerListener, ITosService, IResponseObserver {

    private static final String TAG = "TosService";
    public static final String UTF8 = "UTF8";
    private static SeqGenerator sSeqGenerator = SeqGenerator.getInstance();
    private static final String MODULE_NAME = "watchpay";
    private long mUniqueSeq = -1;

    private int mReqID = -1;

    private IResponseObserver mResponseObserver = null;

    protected String mMouduleName = MODULE_NAME;
    private static int mFreshTokenTimes = 0;
    private String mReqName = REQ_NAME;
    private String mRspName = RSP_NAME;
    protected boolean mNeedReqHeader = true;

    public BaseTosService() {
        mUniqueSeq = sSeqGenerator.uniqueSeq();
    }

    public BaseTosService(String _module, String _req, String _rsp) {
        this();
        mMouduleName = _module;
        mReqName = _req;
        mRspName = _rsp;
    }

    @Override
    public final long getUniqueSeq() {
        return mUniqueSeq;
    }

    @Override
    public boolean invoke(IResponseObserver observer) {
        boolean handled = false;

        // checking
        // if (observer == null) {
        // return handled;
        // }
        mResponseObserver = observer;

        int operType = getOperType();
        if (operType == OPERTYPE_UNKNOWN) {
            return handled;
        }
        JceStruct header = getJceHeader();
        if (header == null && mNeedReqHeader) {
            return handled;
        }
        JceStruct req = getReq(header);
        if (req == null) {
            return handled;
        }

        LogPrint.d(
                TAG,
                String.format("%s,mUniqueSeq:%d req:%s", getClass().getSimpleName(), mUniqueSeq,
                        JceStruct.toDisplaySimpleString(req)));

        UniPacket packet = createReqUnipackageV3(req);

        IServerHandler serverHandler = ServerHandler.getInstance(TsmApi.getGlobalContext());

        serverHandler.registerServerHandlerListener(this);
        serverHandler.setRequestEncrypt(getRequestEncrypt());
        mReqID = serverHandler.reqServer(operType, packet);

        if (mReqID >= 0) {
            handled = true;
        }

        if (!handled) {
            serverHandler.unregisterServerHandlerListener(this);
        }

        return handled;
    }

    protected boolean getRequestEncrypt() {
        return false;
    }

    private final static UniPacket decodePacket(byte[] data) {
        UniPacket packet = new UniPacket();
        packet.setEncodeName(UTF8);
        packet.decode(data);
        return packet;
    }

    @Override
    public final JceStruct parse(UniPacket packet) {
        if (packet == null) {
            return null;
        }

        JceStruct rsp = getRspObject();
        if (rsp == null) {
            return null;
        }
        return packet.getByClass(mRspName, rsp);
    }

    @Override
    public final boolean onResponseSucceed(int reqID, int operType, byte[] response) {
        if (mReqID == reqID) {
            JceStruct rsp = parseJceFromBytes(response);
            if (rsp != null) {
                LogPrint.d(
                        TAG,
                        String.format("%s,mUniqueSeq:%d rsp:%s", getClass().getSimpleName(),
                                mUniqueSeq,
                                JceStruct.toDisplaySimpleString(rsp)));
                int errCode = getSubClassRspIRet(rsp);
                if (handleErrorCode(errCode)) {
                    return false;
                }
                onResponseSucceed(mUniqueSeq, operType, rsp);
            } else {
                // Impossible if code right.
                onResponseFailed(mUniqueSeq, operType, ERR_PARSE_ERROR, "");
            }

            return true;
        }
        return false;
    }

    @Override
    public final boolean onResponseFailed(int reqID, int operType, int errorCode,
            String description) {
        if (mReqID == reqID) {
            LogPrint.d(TAG, String.format("mUniqueSeq:%d errorCode:%d description:%s", mUniqueSeq,
                    errorCode, description));
            if (handleErrorCode(errorCode)) {
                return false;
            }
            onResponseFailed(mUniqueSeq, operType, errorCode, description);
            return true;
        }
        return false;
    }

    @Override
    public final void onResponseSucceed(long uniqueSeq, int operType, JceStruct response) {
        mFreshTokenTimes = 0;
        if (mResponseObserver != null) {
            mResponseObserver.onResponseSucceed(uniqueSeq, operType, response);
        }
    }

    @Override
    public final void onResponseFailed(long uniqueSeq, int operType, int errorCode,
            String description) {
        mFreshTokenTimes = 0;
        if (mResponseObserver != null) {
            mResponseObserver.onResponseFailed(uniqueSeq, operType, errorCode, description);
        }
    }

    private void onHandleTokenInvalid() {
        com.pace.httpserver.HttpServiceChecker.IServerErrChecker checker = HttpServiceChecker.get();
        if (checker != null) {
            checker.onTokenInvalid();
        }
    }

    private int getSubClassRspIRet(JceStruct rsp) {
        int iRet = ERR_PARSE_ERROR;
        Class jce = rsp.getClass();
        Field field = null;
        try {
            field = jce.getDeclaredField("iRet");
            field.setAccessible(true);
            iRet = (Integer) field.get(rsp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return iRet;
    }

    private boolean handleErrorCode(int errorCode) {
        if (errorCode == CommonConstants.WALLET_ACCOUNT_AUTH_FAILED) {
            onHandleTokenInvalid();
            if (mFreshTokenTimes < 2) {
                mFreshTokenTimes++;
                invoke(mResponseObserver);
                return true;
            }
        }
        return false;
    }

    @Override
    public JceStruct invokeSync() {
        JceStruct header = getJceHeader();
        JceStruct req = getReq(header);
        if (req == null) {
            return null;
        }
        IServerHandler serverHandler = ServerHandler.getInstance(TsmApi.getGlobalContext());
        byte[] data = serverHandler.reqServerSync(createReqUnipackageV3(req));
        return parseJceFromBytes(data);
    }

    private UniPacket createReqUnipackageV3(JceStruct req) {
        return QRomWupDataBuilder.createReqUnipackageV3(
                mMouduleName, getFunctionName(),
                mReqName, req);
    }

    private JceStruct parseJceFromBytes(byte[] response) {
        UniPacket packet = decodePacket(response);
        JceStruct rsp = null;
        if (packet != null) {
            rsp = parse(packet);
        }
        return rsp;
    }

    protected abstract JceStruct getJceHeader();

}

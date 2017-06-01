
package com.pace.tsm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pace.common.Tlv;
import com.pace.common.TlvDecode;
import com.pace.common.TransitComprehensive;
import com.pace.tsm.plugin.cards.Constants;
import com.pace.tsm.plugin.utils.ByteUtil;
import com.pace.tsm.plugin.utils.LogUtil;
import com.pace.tsm.plugin.utils.ValueUtil;

import java.util.List;

public class TransactionNotice extends BroadcastReceiver {
    public static final String NXP_ACTION_TRANSACTION_DETECTED = "com.nxp.action.TRANSACTION_DETECTED";
    public static final String NXP_ACTION_TRANSACTION_SEND_DETECTED = "com.pace.tsm.action.TRANSACTION_DETECTED";
    private byte[] aid;
    private byte[] data;
    private String source;

    private static final String TAG = "TransactionNotice";

    class TransParserThread extends Thread {
        final /* synthetic */ TransactionNotice mNotice;
        private Context mContext;
        private Intent mIntent;

        TransParserThread(TransactionNotice transactionNotice) {
            mNotice = transactionNotice;
        }

        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            // ICardParseService fetchCardParseServicePlugin = PluginManager.getInstance()
            // .fetchCardParseServicePlugin(this.b);
            // LogUtil.log(TransactionNotice.a, " cardParseService successfully ");
            // if (fetchCardParseServicePlugin != null) {
            // LogUtil.log(TransactionNotice.a, " fetch plugin execute successfully,continue ");
            // fetchCardParseServicePlugin.consumeBrodcast(this.b, this.c);
            // } else {
            LogUtil.log(TAG, "cardParseService is null，execute local code ");
            this.mNotice.consumeBroadcast(mContext, mIntent);
            // }
            LogUtil.log(TAG, "process broadcast, costtime:"
                    + (System.currentTimeMillis() - currentTimeMillis) + " ms ");
        }

        public void setIntent(Intent intent) {
            mIntent = intent;
        }

        public void setContext(Context context) {
            mContext = context;
        }

    }

    public void onReceive(Context context, Intent intent) {
        LogUtil.log(TAG, " receive broadcas5 ");
        if (intent == null) {
            LogUtil.log(TAG, " intent is null ");
            return;
        }
        TransParserThread thread = new TransParserThread(this);
        thread.setContext(context);
        thread.setIntent(intent);
        thread.start();
    }

    private Tlv fetchTlvFirst(String tag, String str2) {
        if (!(ValueUtil.isEmpty(tag) || ValueUtil.isEmpty(str2))) {
            List<Tlv> buildTlvsForFull = new TlvDecode().buildTlvsForFull(str2);
            if (buildTlvsForFull != null && buildTlvsForFull.size() > 0) {
                for (Tlv tlv : buildTlvsForFull) {
                    if (tlv.getTag().equals(tag)) {
                        return tlv;
                    }
                }
            }
        }
        return null;
    }

    public void consumeBroadcast(Context context, Intent intent) {
        LogUtil.log(TAG, "start recevice consumption broadcast ");
        String action = intent.getAction();
        if (ValueUtil.isEmpty(action) || !action.equals(NXP_ACTION_TRANSACTION_DETECTED)) {
            LogUtil.log(TAG, " didn't find the  action of broadcast ");
            return;
        }
        this.source = intent.getStringExtra("com.nxp.extra.SOURCE");
        boolean isParse = false;
        if (!ValueUtil.isEmpty(this.source) && this.source.equals("com.nxp.smart_mx.ID")) {
            isParse = true;
        }
        if (isParse) {
            LogUtil.log(TAG, " begin parse data from bottomLayer ");
            this.aid = intent.getByteArrayExtra("com.nxp.extra.AID");
            this.data = intent.getByteArrayExtra("com.nxp.extra.DATA");
            if (this.aid == null || this.data == null) {
                LogUtil.log(TAG, "  parse aid  or  data is null   from bottomLayer ");
                return;
            }
            String outAmount = "0";
            String transactionType = "2";
            String balance = "0";
            String currency = "156";
            String instanceId = ByteUtil.toHexString(this.aid);
            String tlvData = ByteUtil.toHexString(this.data);
            boolean isSend = false;
            try {
                String eventType = tlvData.substring(0, 2);
                if (eventType.equals("E2")) {
                    LogUtil.log(TAG, " the transaction is pboc");
                    if (tlvData.substring(12, 14).equals("40")) {
                        LogUtil.loge(TAG, "  current transaction successful ");
                        String tlv = tlvData.substring(58);
                        Tlv amountTlv = fetchTlvFirst("9F02", tlv);
                        Tlv currencyTlv = fetchTlvFirst("5F2A", tlv);
                        Tlv validAmountTlv = fetchTlvFirst("9F79", tlv);
                        if (amountTlv == null || currencyTlv == null || validAmountTlv == null) {
                            LogUtil.loge(TAG, "  any tlv object is null");
                        } else {
                            outAmount = new StringBuilder(
                                    String.valueOf(Long.parseLong(amountTlv.getValue())))
                                            .toString();
                            currency = currencyTlv.getValue().replaceFirst("^0*", "");
                            balance = new StringBuilder(
                                    String.valueOf(Long
                                            .parseLong(new StringBuilder(String.valueOf(
                                                    Long.parseLong(validAmountTlv.getValue())))
                                                            .toString())
                                            - Long.parseLong(outAmount))).toString();
                            isSend = true;
                        }
                    } else {
                        LogUtil.loge(TAG, "  current transaction failure ");
                    }
                } else if (eventType.equals("01")) {
                    LogUtil.log(TAG, " the transaction is bus");
                    outAmount = TransitComprehensive.getAmountFen(tlvData.substring(2, 10));
                    transactionType = tlvData.substring(10, 12);
                    LogUtil.log(TAG, " tlvdata transtype:" + transactionType);
                    if (transactionType.equals("02")
                            || transactionType.equals("01")) {
                        transactionType = "1";
                    } else if (transactionType.equals("05") || transactionType.equals("06")
                            || transactionType.equals("09") || transactionType.equals("11")) {
                        transactionType = "2";
                        isSend = true;
                    }
                    balance = tlvData.substring(38, 46);
                    balance = (instanceId == null
                            || !instanceId.equals(Constants.AID_SZT))
                                    ? TransitComprehensive.getAmountFen(balance)
                                    : new StringBuilder(String
                                            .valueOf(Long.parseLong(balance, 16) - 2147483648L))
                                                    .toString();
                } else if (eventType.equals("10")) {
                    LogUtil.log(TAG, " the transaction is szt");
                    outAmount = TransitComprehensive.getAmountFen(tlvData.substring(6, 14));
                    transactionType = tlvData.substring(4, 6);
                    LogUtil.log(TAG, " tlvdata transtype:" + transactionType);
                    if (transactionType.equals("02")
                            || transactionType.equals("01")) {
                        transactionType = "1";
                    } else if (transactionType.equals("05") || transactionType.equals("06")
                            || transactionType.equals("09") || transactionType.equals("11")) {
                        transactionType = "2";
                        isSend = true;
                    }
                    balance = new StringBuilder(String
                            .valueOf(Long.parseLong(tlvData.substring(14, 22).substring(0, 8), 16)
                                    - 2147483648L)).toString();
                }
                if (isSend) {
                    sendTransactBroadcast(context, instanceId, outAmount, balance, currency,
                            transactionType);
                    return;
                } else {
                    LogUtil.log(TAG, "eventType:" + eventType
                            + " is not equal with E2 and 01 so isSend is false,no send broadcast ");
                    return;
                }
            } catch (Exception e) {
                LogUtil.log(TAG,
                        " parse the data of broadcast happens exception: " + e.getMessage());
                return;
            }
        }
        LogUtil.log(TAG,
                " needn't  parse data from bottomLayer because of source data invalid ");
    }

    private void sendTransactBroadcast(Context context, String instance_id, String amount,
            String balance,
            String currency, String transType) {
        if (ValueUtil.isEmpty(instance_id) || ValueUtil.isEmpty(amount)
                || ValueUtil.isEmpty(balance) || ValueUtil.isEmpty(currency)
                || ValueUtil.isEmpty(transType)) {
            LogUtil.log(this.TAG,
                    " send broadcast failure because of cardId or amount is null or balance is null or currency is null or transType is null ");
            return;
        }
        Intent intent = new Intent(NXP_ACTION_TRANSACTION_SEND_DETECTED);
        intent.putExtra("com.snowballtech.wallet.INSTANCEID", instance_id);
        intent.putExtra("com.snowballtech.wallet.TRANSTYPE", transType);
        intent.putExtra("com.snowballtech.wallet.TRANSCURRENCY", currency);
        intent.putExtra("com.snowballtech.wallet.TRANSAMOUNT", amount);
        intent.putExtra("com.snowballtech.wallet.BALANCE", balance);
        intent.setFlags(32);
        context.sendBroadcast(intent);
        LogUtil.log(this.TAG,
                " send broadcast successful instance_id:" + instance_id + ",amount:" + amount
                        + ",balance:" + balance + ",currency:" + currency + ",transType:"
                        + transType);
    }

}


package com.pace.common;

import android.text.TextUtils;

import com.pace.constants.ApduConstants;

import java.util.List;

public class ApduHelper {
    private static String wrapApduTemplete(String tmplete, String aid) {
        int aidLen = aid.length() / 2;
        String aidSegment = "00".equals(aid) ? "" : ByteUtil.toHex(aidLen);
        int datLen = "00".equals(aid) ? 2 : aid.length() / 2 + 2;
        return tmplete.replace("#", ByteUtil.toHex(datLen)).replace("@", aidSegment)
                .replace("(aid)", aid);
    }

    // public static String listCRSApp() {
    // return getCRSAppStat("00");
    // }

    public static String selectAid(String targetAid) {
        int aidLen = targetAid.length() / 2;
        String aidSegment = ByteUtil.toHex(aidLen);
        String tmplete = ApduConstants.APDU_SELECT_AID;
        return tmplete.replace("#", aidSegment).replace("(aid)", targetAid);
    }

    public static String listCRSApp(int p2) {
        String word = new StringBuilder("0").append(p2).append("#").toString();
        String src = (ApduConstants.APDU_LIST_STATE).replace("00#", word);
        return wrapApduTemplete(src, "00");
    }

    public static String getCRSAppStat(String aid) {
        return wrapApduTemplete(ApduConstants.APDU_LIST_STATE, aid);
    }

    public static String activeAid(String aid) {
        return wrapApduTemplete(ApduConstants.APDU_ACTIVE_APP, aid);
    }

    public static String disactiveAid(String aid) {
        return wrapApduTemplete(ApduConstants.APDU_DISACTIVE_APP, aid);
    }

    public static boolean isResponseSuc(List<String> rsp) {
        // TODO
        return false;
    }

    public static boolean isAidExisit(String apdu, String aid) {
        if (TextUtils.isEmpty(apdu) || TextUtils.isEmpty(aid)) {
            return false;
        }
        return apdu.contains(aid);
    }

    public static int isAppActived(String apdu, String aid) {
        if (TextUtils.isEmpty(apdu) || TextUtils.isEmpty(aid)) {
            return -1;
        }
        byte[] bsApdu = ByteUtil.toByteArray(apdu);
        int aidIndex = apdu.lastIndexOf(aid);
        aidIndex += aid.length() - 1 + 10;
        int offset = aidIndex / 2;
        byte result = bsApdu[offset];
        return (int) result;
    }
    // public static boolea
}

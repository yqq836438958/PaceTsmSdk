
package com.pace.common;

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

    public static String lsCRS(String aid) {
        return wrapApduTemplete(ApduConstants.AID_CRS, aid);
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
}

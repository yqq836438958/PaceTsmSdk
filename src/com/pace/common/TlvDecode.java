
package com.pace.common;

import com.pace.tsm.utils.ValueUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TlvDecode {
    public List<Tlv> buildTlvsForFull(String str) {
        List<Tlv> arrayList = new ArrayList();
        if (!ValueUtil.isEmpty(str)) {
            decodeTLVForFull(str, str.length() / 2, arrayList);
        }
        return arrayList;
    }

    private void decodeTLVForFull(String str, int i, List<Tlv> list) {
        if (list != null && str.substring(0, str.length()).length() > 0) {
            int i2;
            String[] strArr = new String[(str.substring(0, str.length()).length() / 2)];
            for (i2 = 0; i2 < str.substring(0, str.length()).length() / 2; i2++) {
                strArr[i2] = str.substring(0, str.length()).substring(i2 * 2, (i2 * 2) + 2);
            }
            StringBuilder stringBuilder;
            int[] generateLength;
            StringBuilder stringBuilder2;
            if ((Integer.valueOf(strArr[0], 16).intValue() & 32) != 32) {
                if ((Integer.parseInt(strArr[0], 16) & 31) != 31) {
                    stringBuilder = new StringBuilder();
                    generateLength = generateLength(stringBuilder, strArr, 1);
                    list.add(new Tlv(strArr[0], stringBuilder.toString().length(),
                            stringBuilder.toString()));
                    if (i > generateLength[1]) {
                        stringBuilder2 = new StringBuilder();
                        for (i2 = generateLength[1]; i2 < strArr.length; i2++) {
                            stringBuilder2.append(strArr[i2]);
                        }
                        decodeTLVForFull(stringBuilder2.toString(),
                                strArr.length - generateLength[1], list);
                        return;
                    }
                    return;
                }
                stringBuilder = new StringBuilder();
                generateLength = generateLength(stringBuilder, strArr, 2);
                list.add(new Tlv(strArr[0] + strArr[1], stringBuilder.toString().length(),
                        stringBuilder.toString()));
                if (i > generateLength[1]) {
                    stringBuilder2 = new StringBuilder();
                    for (i2 = generateLength[1]; i2 < strArr.length; i2++) {
                        stringBuilder2.append(strArr[i2]);
                    }
                    decodeTLVForFull(stringBuilder2.toString(), strArr.length - generateLength[1],
                            list);
                }
            } else if ((Integer.valueOf(strArr[0], 16).intValue() & 31) != 31) {
                stringBuilder = new StringBuilder();
                generateLength = generateLength(stringBuilder, strArr, 1);
                list.add(new Tlv(strArr[0], stringBuilder.toString().length(),
                        stringBuilder.toString()));
                decodeTLVForFull(stringBuilder.toString(), stringBuilder.length() / 2, list);
                if (i > generateLength[1]) {
                    stringBuilder2 = new StringBuilder();
                    for (i2 = generateLength[1]; i2 < strArr.length; i2++) {
                        stringBuilder2.append(strArr[i2]);
                    }
                    decodeTLVForFull(stringBuilder2.toString(), strArr.length - generateLength[1],
                            list);
                }
            } else {
                stringBuilder = new StringBuilder();
                generateLength = generateLength(stringBuilder, strArr, 2);
                list.add(new Tlv(strArr[0] + strArr[1], stringBuilder.toString().length(),
                        stringBuilder.toString()));
                decodeTLVForFull(stringBuilder.toString(), stringBuilder.length() / 2, list);
                if (i > generateLength[1]) {
                    stringBuilder2 = new StringBuilder();
                    for (i2 = generateLength[1]; i2 < strArr.length; i2++) {
                        stringBuilder2.append(strArr[i2]);
                    }
                    decodeTLVForFull(stringBuilder2.toString(), strArr.length - generateLength[1],
                            list);
                }
            }
        }
    }

    private int[] generateLength(StringBuilder stringBuilder, String[] strArr, int i) {
        int[] iArr = new int[2];
        int[] retriveIndexOfLen = retriveIndexOfLen(strArr, i);
        int i2 = retriveIndexOfLen[1];
        int i3 = retriveIndexOfLen[0];
        int length = i2 + i3 > strArr.length ? strArr.length : i2 + i3;
        for (int i4 = i3; i4 < length; i4++) {
            stringBuilder.append(strArr[i4]);
        }
        iArr[0] = i3;
        iArr[1] = i2 + i3;
        return iArr;
    }

    private static int[] retriveIndexOfLen(String[] strArr, int i) {
        int[] iArr = new int[2];
        if (strArr != null && strArr.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            int binaryToDecimal = binaryToDecimal(strArr[i]);
            iArr[1] = binaryToDecimal;
            if (isDirectValue(strArr[i])) {
                iArr[0] = i + 1;
            } else {
                stringBuilder.setLength(0);
                binaryToDecimal = (i + 1) + binaryToDecimal >= strArr.length ? strArr.length
                        : binaryToDecimal + (i + 1);
                for (int i2 = i + 1; i2 < binaryToDecimal; i2++) {
                    stringBuilder.append(strArr[i2]);
                }
                iArr[1] = Integer.valueOf(stringBuilder.toString(), 16).intValue();
                iArr[0] = binaryToDecimal;
            }
        }
        return iArr;
    }

    private static int binaryToDecimal(String str) {
        if (ValueUtil.isEmpty(str)) {
            return 0;
        }
        return Integer.valueOf(new BigInteger(
                ValueUtil.toBin(Integer.valueOf(str, 16).intValue(), 8, "0").substring(1))
                        .toString(),
                2).intValue();
    }

    private static boolean isDirectValue(String str) {
        if (ValueUtil.isEmpty(str)) {
            return false;
        }
        if (ValueUtil.toBin(Integer.valueOf(str, 16).intValue(), 8, "0").substring(0, 1)
                .equals("1")) {
            return false;
        }
        return true;
    }

    public static void main(String[] strArr) {
        List<Tlv> buildTlvsForFull = new TlvDecode().buildTlvsForFull(
                "6F43840E325041592E5359532E4444463031A531BF0C2E61154F10A00000033301010600030800005A595487010161154F10A00000033301010600030800005A5955870102");
        if (buildTlvsForFull != null && buildTlvsForFull.size() > 0) {
            for (Tlv tlv : buildTlvsForFull) {
                System.out.println(" tlv " + tlv);
            }
        }
    }
}

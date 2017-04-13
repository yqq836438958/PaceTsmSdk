
package com.pace.processor;

import java.util.ArrayList;
import java.util.List;

public class APDU {
    // 用于执行请求的APDU
    private List<String> data;
    private int index;
    private int checkPoint;
    private String sSession = "";// TODO

    public APDU(List<String> list) {
        data = list;
    }

    public APDU(String apdu) {
        data = new ArrayList<String>();
        data.add(apdu);
    }

    public int getSeqId() {
        return index;
    }

    public int getCheckPoint() {
        return checkPoint;
    }

    public List<String> getData() {
        return data;
    }

    public boolean isEmpty() {
        return (data == null) || data.size() <= 0;
    }

    @Override
    public String toString() {
        return "";
    }
}

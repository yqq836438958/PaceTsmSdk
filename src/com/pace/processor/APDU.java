
package com.pace.processor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class APDU implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> data;
    private String sOut;
    private int iRet;

    public APDU(List<String> list) {
        data = list;
    }

    public APDU(String apdu) {
        data = new ArrayList<String>();
        data.add(apdu);
    }

    public List<String> getApdu() {
        return data;
    }

    public String getOut() {
        return sOut;
    }

    public void setOut(String result) {
        sOut = result;
    }

    public List<String> getData() {
        return data;
    }

    public int getRet() {
        return iRet;
    }

    public boolean isEmpty() {
        return (data == null) || data.size() <= 0;
    }

    @Override
    public String toString() {
        return "";
    }
}

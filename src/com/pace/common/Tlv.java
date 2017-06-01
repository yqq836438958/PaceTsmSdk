
package com.pace.common;

public class Tlv {
    private int length;
    private String tag;
    private String value;

    public Tlv(String str, int i, String str2) {
        this.length = i;
        this.tag = str;
        this.value = str2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int i) {
        this.length = i;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String toString() {
        return "tag=[" + this.tag + "]," + "length=[chars:" + this.length + " bytes:"
                + (this.length / 2) + ",hex:" + Integer.toHexString(this.length / 2) + "],"
                + "value=[" + this.value + "]";
    }
}

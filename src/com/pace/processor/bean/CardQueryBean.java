
package com.pace.processor.bean;

public class CardQueryBean {
    private String instance_id;
    private String tag;

    public void setInstance_id(String id) {
        instance_id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public String getTag() {
        return tag;
    }
}

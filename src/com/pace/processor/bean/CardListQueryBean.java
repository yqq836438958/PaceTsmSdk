
package com.pace.processor.bean;

public class CardListQueryBean {
    public static final int INS_PERSON = 2;
    public static final int INS_INSTALL = 1;
    public static final int INS_UNINSTALL = 0;
    public static final int ACT_TRUE = 1;
    public static final int ACT_FALSE = 0;
    private String instance_id;
    private int install_status;
    private int activite_status;

    public void setInstance_id(String aid) {
        this.instance_id = aid;
    }

    public void setInstall_status(int status) {
        this.install_status = status;
    }

    public void setActivte_status(int status) {
        this.activite_status = status;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public int getInstall_status() {
        return install_status;
    }

    public int getActivite_status() {
        return activite_status;
    }
}

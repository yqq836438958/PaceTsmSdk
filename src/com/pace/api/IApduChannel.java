
package com.pace.api;

import java.util.List;

public interface IApduChannel {
    public List<String> transmit(List<String> apdus);

    public String transmit(String apdu);

    public boolean open();

    public void close();
}

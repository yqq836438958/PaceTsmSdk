
package com.pace.api;

public interface IApduChannel {
    public byte[] transmit(byte[] apdu);

    public boolean open();

    public void close();
}

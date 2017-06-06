package com.pace.tsm.service;

interface IPaceApduChannel {
    byte[] transmit(in byte[] apdus);

    boolean open();

    void close();
}
package com.aliyun.linksdk.alcs;

import java.net.InetAddress;

/* JADX INFO: loaded from: classes2.dex */
public class DiscoveryDeviceInfo {
    private InetAddress address;
    private String payload;
    private int port;

    public InetAddress getAddress() {
        return this.address;
    }

    public void setAddress(InetAddress inetAddress) {
        this.address = inetAddress;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int i) {
        this.port = i;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String str) {
        this.payload = str;
    }
}

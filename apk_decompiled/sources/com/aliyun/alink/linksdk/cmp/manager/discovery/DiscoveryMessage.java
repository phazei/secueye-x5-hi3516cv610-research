package com.aliyun.alink.linksdk.cmp.manager.discovery;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class DiscoveryMessage extends AMessage {
    public String deviceName;
    public ARequest discoveryRequest;
    public Map<String, Object> extraData;
    private String ip;
    public String mac;
    public String modelType;
    private int port;
    public String productKey;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String str) {
        this.ip = str;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int i) {
        this.port = i;
    }

    public DiscoveryMessage() {
    }

    public DiscoveryMessage(DiscoveryMessage discoveryMessage) {
        if (discoveryMessage == null || discoveryMessage.hashCode() == hashCode()) {
            return;
        }
        this.ip = discoveryMessage.ip;
        this.port = discoveryMessage.port;
        this.productKey = discoveryMessage.productKey;
        this.deviceName = discoveryMessage.deviceName;
        this.modelType = discoveryMessage.modelType;
        this.mac = discoveryMessage.mac;
        this.discoveryRequest = discoveryMessage.discoveryRequest;
    }
}

package com.aliyun.alink.linksdk.alcs.lpbs.data;

/* JADX INFO: loaded from: classes2.dex */
public class PalSubMessage {
    public PalDeviceInfo deviceInfo;
    public byte[] payload;
    public String topic;

    public String getDevId() {
        PalDeviceInfo palDeviceInfo = this.deviceInfo;
        if (palDeviceInfo != null) {
            return palDeviceInfo.getDevId();
        }
        return null;
    }
}

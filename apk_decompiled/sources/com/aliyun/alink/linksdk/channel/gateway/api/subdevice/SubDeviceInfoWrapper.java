package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

/* JADX INFO: loaded from: classes2.dex */
public class SubDeviceInfoWrapper {
    public ISubDeviceConnectListener connectListener;
    public SubDeviceInfo deviceInfo;

    public SubDeviceInfoWrapper(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener) {
        this.deviceInfo = null;
        this.connectListener = null;
        this.deviceInfo = subDeviceInfo;
        this.connectListener = iSubDeviceConnectListener;
    }
}

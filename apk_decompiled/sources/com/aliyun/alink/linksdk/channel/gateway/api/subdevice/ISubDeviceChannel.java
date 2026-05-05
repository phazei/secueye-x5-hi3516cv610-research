package com.aliyun.alink.linksdk.channel.gateway.api.subdevice;

import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;

/* JADX INFO: loaded from: classes2.dex */
public interface ISubDeviceChannel {
    SubDeviceInfo getSubDeviceInfo();

    void offline(ISubDeviceActionListener iSubDeviceActionListener);

    void online(ISubDeviceActionListener iSubDeviceActionListener);

    void setDeleteListener(IConnectRrpcListener iConnectRrpcListener);

    void setDisableListener(IConnectRrpcListener iConnectRrpcListener);

    void subscribe(String str, ISubDeviceActionListener iSubDeviceActionListener);

    void unSubscribe(String str, ISubDeviceActionListener iSubDeviceActionListener);

    void uploadData(String str, String str2, ISubDeviceActionListener iSubDeviceActionListener);
}

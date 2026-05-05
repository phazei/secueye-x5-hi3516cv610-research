package com.aliyun.alink.linksdk.alcs.api.client;

/* JADX INFO: loaded from: classes2.dex */
public interface IDeviceStateListener {
    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCONNECTED = 0;

    void onDeviceStateChange(int i);
}

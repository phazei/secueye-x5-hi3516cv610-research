package com.aliyun.alink.linksdk.alcs.lpbs.listener;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface PalDeviceStateListener {
    public static final int STATE_CONNECTED = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_DISCONNECTING = 3;

    void onDeviceStateChange(PalDeviceInfo palDeviceInfo, int i);
}

package com.aliyun.alink.linksdk.alcs.lpbs.listener;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public interface PalDiscoveryListener {
    void onDiscoveryDevice(PalDiscoveryDeviceInfo palDiscoveryDeviceInfo);

    void onDiscoveryFinish();
}

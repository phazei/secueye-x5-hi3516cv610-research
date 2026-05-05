package com.aliyun.alink.linksdk.alcs.lpbs.bridge;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IPalProbe {
    void probeDevice(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener);
}

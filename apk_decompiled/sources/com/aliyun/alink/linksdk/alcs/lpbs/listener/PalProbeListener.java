package com.aliyun.alink.linksdk.alcs.lpbs.listener;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;

/* JADX INFO: loaded from: classes2.dex */
public interface PalProbeListener {
    void onComplete(PalDeviceInfo palDeviceInfo, PalProbeResult palProbeResult);
}

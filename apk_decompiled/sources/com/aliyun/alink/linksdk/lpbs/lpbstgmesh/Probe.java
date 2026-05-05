package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class Probe implements IPalProbe {
    private static final String TAG = Utils.TAG + "Probe";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe
    public void probeDevice(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener) {
        ALog.w(TAG, "probeDevice palDeviceInfo :" + palDeviceInfo + " palProbeListener:" + palProbeListener + " PalProbeResult:  PAL_PROBE_ERROR_RSPERROR");
        PalProbeResult palProbeResult = new PalProbeResult(2);
        if (palProbeListener != null) {
            palProbeListener.onComplete(palDeviceInfo, palProbeResult);
        }
    }
}

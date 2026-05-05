package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.api.ICAProbeListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;

/* JADX INFO: compiled from: ICAProbeDevListenerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class l implements ICAProbeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private PalProbeListener f4085a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDeviceInfo f4086b;

    public l(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener) {
        this.f4085a = palProbeListener;
        this.f4086b = palDeviceInfo;
    }

    @Override // com.aliyun.alink.linksdk.alcs.api.ICAProbeListener
    public void onComplete(ICADeviceInfo iCADeviceInfo, int i) {
        PalProbeListener palProbeListener = this.f4085a;
        if (palProbeListener != null) {
            palProbeListener.onComplete(this.f4086b, new PalProbeResult(i, "iot_ica"));
        }
    }
}

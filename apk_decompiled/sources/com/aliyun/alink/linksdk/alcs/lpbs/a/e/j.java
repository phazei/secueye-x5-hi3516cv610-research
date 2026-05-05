package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;

/* JADX INFO: compiled from: PkDnChgeProbeListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class j implements PalProbeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private PalProbeListener f4017a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f4018b;

    public j(PalProbeListener palProbeListener, String str) {
        this.f4017a = palProbeListener;
        this.f4018b = str;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener
    public void onComplete(PalDeviceInfo palDeviceInfo, PalProbeResult palProbeResult) {
        PalDeviceInfo aliIoTPkDn = PluginMgr.getInstance().toAliIoTPkDn(palDeviceInfo, this.f4018b);
        PalProbeListener palProbeListener = this.f4017a;
        if (palProbeListener != null) {
            palProbeListener.onComplete(aliIoTPkDn, palProbeResult);
        }
    }
}

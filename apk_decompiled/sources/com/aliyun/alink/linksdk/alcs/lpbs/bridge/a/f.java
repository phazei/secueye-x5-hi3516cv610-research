package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.data.ica.ICADiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalProbeResult;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalProbeListener;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAAlcsProbe.java */
/* JADX INFO: loaded from: classes2.dex */
public class f implements IPalProbe {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4067a = "[AlcsLPBS]ICAAlcsProbe";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private c f4068b;

    public f(c cVar) {
        this.f4068b = cVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalProbe
    public void probeDevice(PalDeviceInfo palDeviceInfo, PalProbeListener palProbeListener) {
        ALog.d(f4067a, "probeDevice deviceInfo:" + palDeviceInfo + " listener:" + palProbeListener);
        if (palProbeListener == null) {
            ALog.d(f4067a, "probeDevice listener null");
            return;
        }
        ICADiscoveryDeviceInfo iCADiscoveryDeviceInfoA = this.f4068b.a(palDeviceInfo.getDevId());
        if (iCADiscoveryDeviceInfoA == null) {
            ALog.e(f4067a, "probeDevice icaDiscoveryDeviceInfo null");
            palProbeListener.onComplete(palDeviceInfo, new PalProbeResult(2));
        } else {
            ICAAlcsNative.probeDevice(iCADiscoveryDeviceInfoA.addr, iCADiscoveryDeviceInfoA.port, iCADiscoveryDeviceInfoA.deviceInfo, new l(palDeviceInfo, palProbeListener));
        }
    }
}

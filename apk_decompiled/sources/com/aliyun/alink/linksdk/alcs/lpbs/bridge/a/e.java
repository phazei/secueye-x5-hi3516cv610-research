package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAAlcsDiscovery.java */
/* JADX INFO: loaded from: classes2.dex */
public class e implements IPalDiscovery {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4065b = "[AlcsLPBS]ICAAlcsDiscovery";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private c f4066a;

    public e(c cVar) {
        this.f4066a = cVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryListener palDiscoveryListener) {
        ALog.d(f4065b, "startDiscovery timeOut:" + i + " listener:" + palDiscoveryListener);
        return ICAAlcsNative.discoveryDevice(i, new i(this.f4066a, palDiscoveryListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryConfig palDiscoveryConfig, PalDiscoveryListener palDiscoveryListener) {
        ALog.d(f4065b, "startDiscovery timeOut:" + i + " PalDiscoveryConfig:" + palDiscoveryConfig + " listener:" + palDiscoveryListener);
        return ICAAlcsNative.discoveryDevice(i, new i(this.f4066a, palDiscoveryListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopDiscovery() {
        ALog.d(f4065b, "stopDiscovery");
        return ICAAlcsNative.stopDiscoveryDevice();
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startNotifyMonitor(PalDiscoveryListener palDiscoveryListener) {
        ALog.d(f4065b, "startNotifyMonitor listener:" + palDiscoveryListener);
        return ICAAlcsNative.regDeviceNotifyListener(new i(this.f4066a, palDiscoveryListener));
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopNotifyMonitor() {
        ALog.d(f4065b, "stopNotifyMonitor");
        return ICAAlcsNative.unregDeviceNotifyListener();
    }
}

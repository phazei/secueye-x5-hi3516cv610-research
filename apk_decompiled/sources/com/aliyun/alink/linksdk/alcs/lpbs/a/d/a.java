package com.aliyun.alink.linksdk.alcs.lpbs.a.d;

import com.aliyun.alink.linksdk.alcs.lpbs.a.e.g;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: DeviceStateListenerMgr.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3963a = "[AlcsLPBS]DeviceStateListenerMgr";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.b.a f3964b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f3965c;

    public a(com.aliyun.alink.linksdk.alcs.lpbs.a.b.a aVar, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar2) {
        this.f3964b = aVar;
        this.f3965c = aVar2;
    }

    public boolean a(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        if (palDeviceStateListener == null || palDeviceInfo == null) {
            ALog.e(f3963a, "regDeviceStateListener listener deviceInfo null");
            return false;
        }
        g gVar = new g(palDeviceInfo, this.f3965c, palDeviceStateListener);
        ALog.d(f3963a, "regDeviceStateListener " + palDeviceStateListener.hashCode() + " listenerProxy:" + gVar.hashCode());
        IPalConnect iPalConnectB = this.f3964b.b(palDeviceInfo.getDevId());
        if (iPalConnectB == null) {
            ALog.e(f3963a, "regDeviceStateListener connect null");
            return false;
        }
        iPalConnectB.regDeviceStateListener(PluginMgr.getInstance().toPrivatePkDn(palDeviceInfo, iPalConnectB.getPluginId()), gVar);
        return true;
    }

    public boolean b(PalDeviceInfo palDeviceInfo, PalDeviceStateListener palDeviceStateListener) {
        if (palDeviceStateListener == null || palDeviceInfo == null) {
            ALog.e(f3963a, "unregDeviceStateListener listener deviceInfo null");
            return false;
        }
        ALog.d(f3963a, "unregDeviceStateListener listener:" + palDeviceStateListener.hashCode());
        IPalConnect iPalConnectB = this.f3964b.b(palDeviceInfo.getDevId());
        if (iPalConnectB == null) {
            ALog.e(f3963a, "unregDeviceStateListener connect null");
            return false;
        }
        iPalConnectB.unregDeviceStateListener(PluginMgr.getInstance().toPrivatePkDn(palDeviceInfo, iPalConnectB.getPluginId()), palDeviceStateListener);
        return true;
    }
}

package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: PalDevStateListenerProxy.java */
/* JADX INFO: loaded from: classes2.dex */
public class g implements PalDeviceStateListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4008a = "[AlcsLPBS]PalDevStateListenerProxy";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDeviceStateListener f4009b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PalDeviceInfo f4010c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private com.aliyun.alink.linksdk.alcs.lpbs.a.a.a f4011d;

    public g(PalDeviceInfo palDeviceInfo, com.aliyun.alink.linksdk.alcs.lpbs.a.a.a aVar, PalDeviceStateListener palDeviceStateListener) {
        this.f4009b = palDeviceStateListener;
        this.f4010c = palDeviceInfo;
        this.f4011d = aVar;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener
    public void onDeviceStateChange(PalDeviceInfo palDeviceInfo, int i) {
        if (palDeviceInfo == null) {
            ALog.e(f4008a, "deviceInfo null state:" + i);
            return;
        }
        ALog.d(f4008a, "onDeviceStateChange deviceInfo:" + palDeviceInfo.toString() + " state:" + i + " mDeviceInfo:" + this.f4010c.toString());
        PalDeviceStateListener palDeviceStateListener = this.f4009b;
        if (palDeviceStateListener != null) {
            palDeviceStateListener.onDeviceStateChange(this.f4010c, i);
        }
        if (i == 0 && PluginMgr.getInstance().isDataToCloud(this.f4010c)) {
            this.f4011d.a(this.f4010c);
        }
    }
}

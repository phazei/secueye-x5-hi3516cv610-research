package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import java.util.Map;

/* JADX INFO: compiled from: PkDnChgeConnectListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class i implements PalConnectListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private PalConnectListener f4015a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f4016b;

    public i(PalConnectListener palConnectListener, String str) {
        this.f4015a = palConnectListener;
        this.f4016b = str;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener
    public void onLoad(int i, Map<String, Object> map, PalDeviceInfo palDeviceInfo) {
        PalDeviceInfo aliIoTPkDn = PluginMgr.getInstance().toAliIoTPkDn(palDeviceInfo, this.f4016b);
        PalConnectListener palConnectListener = this.f4015a;
        if (palConnectListener != null) {
            palConnectListener.onLoad(i, map, aliIoTPkDn);
        }
    }
}

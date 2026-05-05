package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: DiscoveryPkDnChgListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements PalDiscoveryListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3988a = "[AlcsLPBS]DiscoveryPkDnChgListener";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalDiscoveryListener f3989b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f3990c;

    public d(String str, PalDiscoveryListener palDiscoveryListener) {
        this.f3989b = palDiscoveryListener;
        this.f3990c = str;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
    public void onDiscoveryDevice(final PalDiscoveryDeviceInfo palDiscoveryDeviceInfo) {
        if (this.f3989b == null || palDiscoveryDeviceInfo == null) {
            ALog.e(f3988a, "onDiscoveryDevice mListener or discoveryDeviceInfo null");
            return;
        }
        ALog.d(f3988a, "onDiscoveryDevice discoveryDeviceInfo:" + palDiscoveryDeviceInfo.getDevId() + " isPkDnNeedConvert:" + palDiscoveryDeviceInfo.isPkDnNeedConvert());
        if (palDiscoveryDeviceInfo.isPkDnNeedConvert()) {
            PluginMgr.getInstance().initDevTrans(palDiscoveryDeviceInfo, new IDevInfoTrans.IDevInfoTransListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.d.1
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans.IDevInfoTransListener
                public void onComplete(boolean z, Object obj) {
                    ALog.d(d.f3988a, "initDevTrans onComplete isSuccess:" + z + " id:" + palDiscoveryDeviceInfo.getDevId());
                    if (!z) {
                        ALog.e(d.f3988a, "initDevTrans onComplete isSuccess error onDiscoveryDevice not Success");
                        return;
                    }
                    palDiscoveryDeviceInfo.deviceInfo = PluginMgr.getInstance().toAliIoTPkDn(palDiscoveryDeviceInfo.deviceInfo, d.this.f3990c);
                    palDiscoveryDeviceInfo.pluginId = d.this.f3990c;
                    PluginMgr.getInstance().insertDiscoveryDev(palDiscoveryDeviceInfo.deviceInfo.getDevId(), d.this.f3990c, palDiscoveryDeviceInfo);
                    d.this.f3989b.onDiscoveryDevice(palDiscoveryDeviceInfo);
                }
            });
            return;
        }
        palDiscoveryDeviceInfo.pluginId = this.f3990c;
        PluginMgr.getInstance().insertDiscoveryDev(palDiscoveryDeviceInfo.deviceInfo.getDevId(), this.f3990c, palDiscoveryDeviceInfo);
        this.f3989b.onDiscoveryDevice(palDiscoveryDeviceInfo);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener
    public void onDiscoveryFinish() {
        PalDiscoveryListener palDiscoveryListener = this.f3989b;
        if (palDiscoveryListener != null) {
            palDiscoveryListener.onDiscoveryFinish();
        }
    }
}

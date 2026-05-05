package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class Discovery implements IPalDiscovery {
    private static final String TAG = Utils.TAG + "Discovery";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryListener palDiscoveryListener) {
        ALog.d(TAG, "startDiscovery timeout" + i);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startDiscovery(int i, PalDiscoveryConfig palDiscoveryConfig, PalDiscoveryListener palDiscoveryListener) {
        ALog.d(TAG, "startDiscovery timeout" + i + " palDiscoveryConfig:" + palDiscoveryConfig);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopDiscovery() {
        ALog.d(TAG, "stopDiscovery");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean startNotifyMonitor(PalDiscoveryListener palDiscoveryListener) {
        ALog.d(TAG, "startNotifyMonitor");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalDiscovery
    public boolean stopNotifyMonitor() {
        ALog.d(TAG, "stopNotifyMonitor");
        return false;
    }
}

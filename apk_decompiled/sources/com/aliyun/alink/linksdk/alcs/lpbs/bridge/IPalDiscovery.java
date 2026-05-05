package com.aliyun.alink.linksdk.alcs.lpbs.bridge;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryConfig;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDiscoveryListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IPalDiscovery {
    public static final int DISCOVERY_TYPE_FINISH = 0;
    public static final int DISCOVERY_TYPE_FOUND = 1;

    boolean startDiscovery(int i, PalDiscoveryConfig palDiscoveryConfig, PalDiscoveryListener palDiscoveryListener);

    boolean startDiscovery(int i, PalDiscoveryListener palDiscoveryListener);

    boolean startNotifyMonitor(PalDiscoveryListener palDiscoveryListener);

    boolean stopDiscovery();

    boolean stopNotifyMonitor();
}

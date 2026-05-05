package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.DiscoveryConfig;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectDiscovery {
    void discoveryCertainDevice(ARequest aRequest, IDiscoveryListener iDiscoveryListener);

    void startDiscovery(int i, DiscoveryConfig discoveryConfig, IDiscoveryListener iDiscoveryListener);

    void startDiscovery(int i, IDiscoveryListener iDiscoveryListener);

    void startDiscovery(IDiscoveryListener iDiscoveryListener);

    void startNotifyMonitor(IDiscoveryListener iDiscoveryListener);

    void stopDiscovery();

    void stopNotifyMonitor();
}

package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryMessage;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IDiscoveryListener {
    void onDiscovery(DiscoveryMessage discoveryMessage);

    void onFailure(AError aError);
}

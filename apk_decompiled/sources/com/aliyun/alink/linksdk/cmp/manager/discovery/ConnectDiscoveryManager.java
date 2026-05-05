package com.aliyun.alink.linksdk.cmp.manager.discovery;

import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsDiscoveryConnect;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectDiscovery;
import com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectDiscoveryManager {
    private static final String TAG = "ConnectDiscoveryManager";

    public static IConnectDiscovery getAlcsDiscovery() {
        return getDiscovery(AlcsDiscoveryConnect.CONNECT_ID);
    }

    public static IConnectDiscovery getDiscovery(String str) {
        Object connect = ConnectManager.getInstance().getConnect(str);
        if (connect == null || !(connect instanceof IConnectDiscovery)) {
            return null;
        }
        return (IConnectDiscovery) connect;
    }
}

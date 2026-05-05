package com.aliyun.alink.linksdk.cmp.manager.resource;

import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnect;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnect;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister;
import com.aliyun.alink.linksdk.cmp.manager.connect.ConnectManager;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectResourceManger {
    private static final String TAG = "ConnectResourceManger";

    public static IConnectResourceRegister getAlcsResourceRegister() {
        return getResourceRegister(AlcsServerConnect.CONNECT_ID);
    }

    public static IConnectResourceRegister getPersistentResourceRegister() {
        return getResourceRegister(PersistentConnect.CONNECT_ID);
    }

    public static IConnectResourceRegister getResourceRegister(String str) {
        Object connect = ConnectManager.getInstance().getConnect(str);
        if (connect == null || !(connect instanceof IConnectResourceRegister)) {
            return null;
        }
        return (IConnectResourceRegister) connect;
    }
}

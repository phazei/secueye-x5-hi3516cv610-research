package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectNotifyListener {
    void onConnectStateChange(String str, ConnectState connectState);

    void onNotify(String str, String str2, AMessage aMessage);

    boolean shouldHandle(String str, String str2);
}

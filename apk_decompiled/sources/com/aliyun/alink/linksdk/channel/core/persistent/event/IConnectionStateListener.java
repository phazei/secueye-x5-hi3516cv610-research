package com.aliyun.alink.linksdk.channel.core.persistent.event;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectionStateListener {
    void onConnectFail(String str);

    void onConnected();

    void onDisconnect();
}

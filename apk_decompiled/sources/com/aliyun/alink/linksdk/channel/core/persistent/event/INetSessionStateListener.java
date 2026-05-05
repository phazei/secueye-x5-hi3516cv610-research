package com.aliyun.alink.linksdk.channel.core.persistent.event;

/* JADX INFO: loaded from: classes2.dex */
public interface INetSessionStateListener {
    void onNeedLogin();

    void onSessionEffective();

    void onSessionInvalid();
}

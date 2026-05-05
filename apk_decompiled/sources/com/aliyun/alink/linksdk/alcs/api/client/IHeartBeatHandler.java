package com.aliyun.alink.linksdk.alcs.api.client;

/* JADX INFO: loaded from: classes2.dex */
public interface IHeartBeatHandler {
    void onBeat(String str, int i);

    void onTimeout(String str, int i);
}

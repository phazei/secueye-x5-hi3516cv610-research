package com.aliyun.alink.linksdk.channel.mobile.api;

/* JADX INFO: loaded from: classes2.dex */
public interface IMobileDownstreamListener {
    void onCommand(String str, String str2);

    boolean shouldHandle(String str);
}

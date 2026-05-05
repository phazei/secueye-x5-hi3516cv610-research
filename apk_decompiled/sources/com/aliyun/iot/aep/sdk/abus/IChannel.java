package com.aliyun.iot.aep.sdk.abus;

/* JADX INFO: loaded from: classes2.dex */
public interface IChannel {
    void blockChannel(boolean z);

    void cancelChannel();

    int getChannelID();
}

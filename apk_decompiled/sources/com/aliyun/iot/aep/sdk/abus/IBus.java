package com.aliyun.iot.aep.sdk.abus;

/* JADX INFO: loaded from: classes2.dex */
public interface IBus {
    void attachListener(int i, Object obj, String str, Class<? extends Object> cls);

    void blockChannel(int i, boolean z);

    void cancelChannel(int i);

    void detachListener(int i, Object obj);

    void detachListener(int i, Object obj, Class<? extends Object> cls);

    void postEvent(int i, Object obj);
}

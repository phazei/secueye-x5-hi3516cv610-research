package com.aliyun.iot.aep.sdk.framework.base;

/* JADX INFO: loaded from: classes2.dex */
public interface DataCallBack<T> {
    void onFail(int i, String str);

    void onSuccess(T t);
}

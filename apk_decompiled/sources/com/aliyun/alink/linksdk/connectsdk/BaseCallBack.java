package com.aliyun.alink.linksdk.connectsdk;

/* JADX INFO: loaded from: classes2.dex */
public interface BaseCallBack<T> {
    void onFail(int i, String str);

    void onSuccess(T t);
}

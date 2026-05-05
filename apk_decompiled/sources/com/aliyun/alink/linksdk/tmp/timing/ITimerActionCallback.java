package com.aliyun.alink.linksdk.tmp.timing;

/* JADX INFO: loaded from: classes2.dex */
public interface ITimerActionCallback<T> {
    void onFailure(int i, String str);

    void onSuccess(T t);
}

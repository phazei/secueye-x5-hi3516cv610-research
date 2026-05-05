package com.aliyun.alink.business.devicecenter.channel.http;

/* JADX INFO: loaded from: classes.dex */
public interface IRequestCallback<T> {
    void onFail(DCError dCError, T t);

    void onSuccess(T t);
}

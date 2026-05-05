package com.aliyun.alink.business.devicecenter.config;

/* JADX INFO: loaded from: classes.dex */
public interface IDataCallback<T> {
    void onResult(boolean z, T t);

    void onState(String str, String str2);
}

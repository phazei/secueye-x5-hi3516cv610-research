package com.alibaba.ailabs.iot.mesh.callback;

/* JADX INFO: loaded from: classes.dex */
public interface ConfigActionListener<T> {
    void onFailure(String str, int i, String str2);

    void onSuccess(String str, T t);
}

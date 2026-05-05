package com.alibaba.ailabs.iot.aisbase.callback;

/* JADX INFO: loaded from: classes.dex */
public interface IActionListener<T> {
    void onFailure(int i, String str);

    void onSuccess(T t);
}

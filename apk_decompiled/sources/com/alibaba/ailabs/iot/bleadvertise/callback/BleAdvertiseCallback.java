package com.alibaba.ailabs.iot.bleadvertise.callback;

/* JADX INFO: loaded from: classes.dex */
public interface BleAdvertiseCallback<T> {
    void onFailure(int i, String str);

    void onSuccess(T t);
}

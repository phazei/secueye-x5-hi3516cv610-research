package com.alibaba.ailabs.iot.aisbase.callback;

/* JADX INFO: loaded from: classes.dex */
public interface ICommandActionListener {
    void onError();

    void onNotify(byte[] bArr);

    void onResponse(byte[] bArr);
}

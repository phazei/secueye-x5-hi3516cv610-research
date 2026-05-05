package com.taobao.accs;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public interface IServiceReceiver {
    void onBind(String str, int i);

    void onData(String str, String str2, String str3, byte[] bArr);

    void onResponse(String str, String str2, int i, byte[] bArr);

    void onSendData(String str, String str2, int i);

    void onUnbind(String str, int i);
}

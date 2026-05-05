package com.taobao.accs;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public interface ConnectionListener {
    void onConnect();

    void onDisconnect(int i, String str);
}

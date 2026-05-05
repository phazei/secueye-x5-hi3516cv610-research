package com.taobao.accs;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class IAppReceiverV2 extends IAppReceiverV1 {
    public abstract void onBindApp(int i, String str, String str2);

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onBindUser(String str, int i) {
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindApp(int i) {
    }

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindUser(int i) {
    }

    @Override // com.taobao.accs.IAppReceiverV1
    @Deprecated
    public void onBindApp(int i, String str) {
        onBindApp(i, "", str);
    }

    public void onUnbindApp(int i, String str) {
        onUnbindApp(i);
    }

    public void onBindUser(String str, int i, String str2) {
        onBindUser(str, i);
    }

    public void onUnbindUser(int i, String str) {
        onUnbindUser(i);
    }
}

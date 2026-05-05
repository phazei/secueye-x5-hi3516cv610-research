package com.taobao.accs;

import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class IAgooAppReceiver extends IAppReceiverV2 {
    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public Map<String, String> getAllServices() {
        return null;
    }

    public abstract String getAppkey();

    @Override // com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public String getService(String str) {
        return null;
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onBindUser(String str, int i) {
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindApp(int i) {
    }

    @Override // com.taobao.accs.IAppReceiverV2, com.taobao.accs.IAppReceiverV1, com.taobao.accs.IAppReceiver
    public void onUnbindUser(int i) {
    }
}

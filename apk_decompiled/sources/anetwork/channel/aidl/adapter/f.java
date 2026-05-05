package anetwork.channel.aidl.adapter;

import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class f implements Runnable {
    f() {
    }

    @Override // java.lang.Runnable
    public void run() {
        if (d.f1988c) {
            d.f1988c = false;
            ALog.e("anet.RemoteGetter", "binding service timeout. reset status!", null, new Object[0]);
        }
    }
}

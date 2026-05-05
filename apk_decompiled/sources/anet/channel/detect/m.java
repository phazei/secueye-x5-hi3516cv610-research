package anet.channel.detect;

import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ NetworkStatusHelper.NetworkStatus f1712a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ l f1713b;

    m(l lVar, NetworkStatusHelper.NetworkStatus networkStatus) {
        this.f1713b = lVar;
        this.f1712a = networkStatus;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.f1712a != NetworkStatusHelper.NetworkStatus.NO && this.f1712a != NetworkStatusHelper.NetworkStatus.NONE) {
                this.f1713b.f1711a.a(NetworkStatusHelper.getUniqueId(this.f1712a));
            }
        } catch (Throwable th) {
            ALog.e("anet.MTUDetector", "MTU detecet fail.", null, th, new Object[0]);
        }
    }
}

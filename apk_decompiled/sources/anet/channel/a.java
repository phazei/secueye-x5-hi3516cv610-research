package anet.channel;

import android.content.Intent;
import anet.channel.util.ALog;
import java.util.Iterator;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class a implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Intent f1656a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ AccsSessionManager f1657b;

    a(AccsSessionManager accsSessionManager, Intent intent) {
        this.f1657b = accsSessionManager;
        this.f1656a = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        Iterator it = AccsSessionManager.f1613c.iterator();
        while (it.hasNext()) {
            try {
                ((ISessionListener) it.next()).onConnectionChanged(this.f1656a);
            } catch (Exception e) {
                ALog.e("awcn.AccsSessionManager", "notifyListener exception.", null, e, new Object[0]);
            }
        }
    }
}

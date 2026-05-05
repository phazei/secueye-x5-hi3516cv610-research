package anet.channel.status;

import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class a implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ NetworkStatusHelper.NetworkStatus f1832a;

    a(NetworkStatusHelper.NetworkStatus networkStatus) {
        this.f1832a = networkStatus;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            for (NetworkStatusHelper.INetworkStatusChangeListener iNetworkStatusChangeListener : NetworkStatusHelper.listeners) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                iNetworkStatusChangeListener.onNetworkStatusChanged(this.f1832a);
                if (System.currentTimeMillis() - jCurrentTimeMillis > 500) {
                    ALog.e("awcn.NetworkStatusHelper", "call back cost too much time", null, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, iNetworkStatusChangeListener);
                }
            }
        } catch (Exception unused) {
        }
    }
}

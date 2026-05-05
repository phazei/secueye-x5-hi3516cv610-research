package anetwork.channel.aidl.adapter;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class e implements ServiceConnection {
    e() {
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.RemoteGetter", "ANet_Service Disconnected", null, new Object[0]);
        }
        d.f1986a = null;
        d.f1988c = false;
        if (d.f1989d != null) {
            d.f1989d.countDown();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.RemoteGetter", "[onServiceConnected]ANet_Service start success. ANet run with service mode", null, new Object[0]);
        }
        synchronized (d.class) {
            d.f1986a = IRemoteNetworkGetter.Stub.asInterface(iBinder);
            if (d.f1989d != null) {
                d.f1989d.countDown();
            }
        }
        d.f1987b = false;
        d.f1988c = false;
    }
}

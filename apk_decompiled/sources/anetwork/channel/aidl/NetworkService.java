package anetwork.channel.aidl;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.aidl.IRemoteNetworkGetter;
import anetwork.channel.aidl.RemoteNetwork;
import anetwork.channel.degrade.DegradableNetworkDelegate;
import anetwork.channel.http.HttpNetworkDelegate;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkService extends Service {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f1972b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private RemoteNetwork.Stub f1973c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private RemoteNetwork.Stub f1974d = null;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    IRemoteNetworkGetter.Stub f1971a = new IRemoteNetworkGetter.Stub() { // from class: anetwork.channel.aidl.NetworkService.1
        @Override // anetwork.channel.aidl.IRemoteNetworkGetter
        public RemoteNetwork get(int i) throws RemoteException {
            return i == 1 ? NetworkService.this.f1973c : NetworkService.this.f1974d;
        }
    };

    @Override // android.app.Service
    public void onDestroy() {
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 2;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        this.f1972b = getApplicationContext();
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.NetworkService", "onBind:" + intent.getAction(), null, new Object[0]);
        }
        this.f1973c = new DegradableNetworkDelegate(this.f1972b);
        this.f1974d = new HttpNetworkDelegate(this.f1972b);
        if (IRemoteNetworkGetter.class.getName().equals(intent.getAction())) {
            return this.f1971a;
        }
        return null;
    }
}

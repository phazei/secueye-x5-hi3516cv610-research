package anetwork.channel.entity;

import android.os.RemoteException;
import anetwork.channel.aidl.ParcelableHeader;
import anetwork.channel.aidl.ParcelableNetworkListener;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ ParcelableNetworkListener f2033a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f2034b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ Map f2035c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ c f2036d;

    d(c cVar, ParcelableNetworkListener parcelableNetworkListener, int i, Map map) {
        this.f2036d = cVar;
        this.f2033a = parcelableNetworkListener;
        this.f2034b = i;
        this.f2035c = map;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f2033a.onResponseCode(this.f2034b, new ParcelableHeader(this.f2034b, this.f2035c));
        } catch (RemoteException unused) {
        }
    }
}

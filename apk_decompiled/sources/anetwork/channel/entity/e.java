package anetwork.channel.entity;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anetwork.channel.aidl.DefaultProgressEvent;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.aidl.adapter.ParcelableInputStreamImpl;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f2037a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ ByteArray f2038b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ int f2039c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    final /* synthetic */ ParcelableNetworkListener f2040d;
    final /* synthetic */ c e;

    e(c cVar, int i, ByteArray byteArray, int i2, ParcelableNetworkListener parcelableNetworkListener) {
        this.e = cVar;
        this.f2037a = i;
        this.f2038b = byteArray;
        this.f2039c = i2;
        this.f2040d = parcelableNetworkListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.e.f2032d) {
                try {
                    if (this.e.f2031c != null) {
                        this.e.f2031c.write(this.f2038b);
                    } else {
                        this.e.f2031c = new ParcelableInputStreamImpl();
                        this.e.f2031c.init(this.e.e, this.f2039c);
                        this.e.f2031c.write(this.f2038b);
                        this.f2040d.onInputStreamGet(this.e.f2031c);
                    }
                } catch (Exception unused) {
                    if (this.e.f2031c == null) {
                    } else {
                        this.e.f2031c.close();
                    }
                }
            } else {
                this.f2040d.onDataReceived(new DefaultProgressEvent(this.f2037a, this.f2038b.getDataLength(), this.f2039c, this.f2038b.getBuffer()));
            }
        } catch (RemoteException unused2) {
        }
    }
}

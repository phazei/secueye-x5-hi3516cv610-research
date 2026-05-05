package anetwork.channel.entity;

import android.os.RemoteException;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.aidl.ParcelableNetworkListener;
import anetwork.channel.aidl.adapter.ParcelableInputStreamImpl;
import anetwork.channel.interceptor.Callback;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c implements Callback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ParcelableNetworkListener f2029a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f2030b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ParcelableInputStreamImpl f2031c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f2032d;
    private g e;

    public c(ParcelableNetworkListener parcelableNetworkListener, g gVar) {
        this.f2032d = false;
        this.e = null;
        this.f2029a = parcelableNetworkListener;
        this.e = gVar;
        if (parcelableNetworkListener != null) {
            try {
                if ((parcelableNetworkListener.getListenerState() & 8) != 0) {
                    this.f2032d = true;
                }
            } catch (RemoteException unused) {
            }
        }
    }

    @Override // anetwork.channel.interceptor.Callback
    public void onResponseCode(int i, Map<String, List<String>> map) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.Repeater", "[onResponseCode]", this.f2030b, new Object[0]);
        }
        ParcelableNetworkListener parcelableNetworkListener = this.f2029a;
        if (parcelableNetworkListener != null) {
            a(new d(this, parcelableNetworkListener, i, map));
        }
    }

    @Override // anetwork.channel.interceptor.Callback
    public void onDataReceiveSize(int i, int i2, ByteArray byteArray) {
        ParcelableNetworkListener parcelableNetworkListener = this.f2029a;
        if (parcelableNetworkListener != null) {
            a(new e(this, i, byteArray, i2, parcelableNetworkListener));
        }
    }

    @Override // anetwork.channel.interceptor.Callback
    public void onFinish(DefaultFinishEvent defaultFinishEvent) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.Repeater", "[onFinish] ", this.f2030b, new Object[0]);
        }
        ParcelableNetworkListener parcelableNetworkListener = this.f2029a;
        if (parcelableNetworkListener != null) {
            f fVar = new f(this, defaultFinishEvent, parcelableNetworkListener);
            RequestStatistic requestStatistic = defaultFinishEvent.rs;
            if (requestStatistic != null) {
                requestStatistic.rspCbDispatch = System.currentTimeMillis();
            }
            a(fVar);
        }
        this.f2029a = null;
    }

    private void a(Runnable runnable) {
        if (this.e.c()) {
            runnable.run();
        } else {
            String str = this.f2030b;
            a.a(str != null ? str.hashCode() : hashCode(), runnable);
        }
    }

    public void a(String str) {
        this.f2030b = str;
    }
}

package anetwork.channel.aidl.adapter;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ byte f1983a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ Object f1984b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ ParcelableNetworkListenerWrapper f1985c;

    c(ParcelableNetworkListenerWrapper parcelableNetworkListenerWrapper, byte b2, Object obj) {
        this.f1985c = parcelableNetworkListenerWrapper;
        this.f1983a = b2;
        this.f1984b = obj;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1985c.dispatchCallback(this.f1983a, this.f1984b);
    }
}

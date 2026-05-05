package b.f;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2172a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f2173b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ byte[] f2174c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ byte[] f2175d;
    public final /* synthetic */ int e;
    public final /* synthetic */ e f;

    public c(e eVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        this.f = eVar;
        this.f2172a = i;
        this.f2173b = i2;
        this.f2174c = bArr;
        this.f2175d = bArr2;
        this.e = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f.sendBlockAck(this.f2172a, this.f2173b, this.f2174c, this.f2175d, this.e);
    }
}

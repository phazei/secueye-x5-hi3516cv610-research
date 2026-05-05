package b.f;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f2176a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f2177b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ byte[] f2178c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ byte[] f2179d;
    public final /* synthetic */ int e;
    public final /* synthetic */ e f;

    public d(e eVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        this.f = eVar;
        this.f2176a = i;
        this.f2177b = i2;
        this.f2178c = bArr;
        this.f2179d = bArr2;
        this.e = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f.sendBlockAck(this.f2176a, this.f2177b, this.f2178c, this.f2179d, this.e);
    }
}

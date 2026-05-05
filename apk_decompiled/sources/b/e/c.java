package b.e;

import b.e.e;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ e.a f2158a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f2159b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f2160c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ byte[] f2161d;
    public final /* synthetic */ byte[] e;
    public final /* synthetic */ int f;
    public final /* synthetic */ e g;

    public c(e eVar, e.a aVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        this.g = eVar;
        this.f2158a = aVar;
        this.f2159b = i;
        this.f2160c = i2;
        this.f2161d = bArr;
        this.e = bArr2;
        this.f = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.g.c(this.f2158a, this.f2159b, this.f2160c, this.f2161d, this.e, this.f);
    }
}

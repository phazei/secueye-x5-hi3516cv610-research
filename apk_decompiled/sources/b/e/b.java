package b.e;

import b.e.e;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ e.a f2154a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f2155b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f2156c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ byte[] f2157d;
    public final /* synthetic */ byte[] e;
    public final /* synthetic */ int f;
    public final /* synthetic */ e g;

    public b(e eVar, e.a aVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        this.g = eVar;
        this.f2154a = aVar;
        this.f2155b = i;
        this.f2156c = i2;
        this.f2157d = bArr;
        this.e = bArr2;
        this.f = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.g.c(this.f2154a, this.f2155b, this.f2156c, this.f2157d, this.e, this.f);
    }
}

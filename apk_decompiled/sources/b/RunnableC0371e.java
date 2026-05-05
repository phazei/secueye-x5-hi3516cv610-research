package b;

/* JADX INFO: renamed from: b.e, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0371e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ byte[] f2148a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ RunnableC0372f f2149b;

    public RunnableC0371e(RunnableC0372f runnableC0372f, byte[] bArr) {
        this.f2149b = runnableC0372f;
        this.f2148a = bArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2149b.f2170c.f.sendPdu(this.f2149b.f2169b, this.f2148a);
    }
}

package a.a.a.a.b.l;

import com.alibaba.ailabs.iot.mesh.ut.UtTraceInfo;

/* JADX INFO: compiled from: UtTraceManager.java */
/* JADX INFO: loaded from: classes.dex */
public class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f1484a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ UtTraceInfo f1485b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ f f1486c;

    public e(f fVar, String str, UtTraceInfo utTraceInfo) {
        this.f1486c = fVar;
        this.f1484a = str;
        this.f1485b = utTraceInfo;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f1486c.a(this.f1484a, this.f1485b);
    }
}

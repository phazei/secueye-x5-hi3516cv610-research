package b;

import android.annotation.SuppressLint;
import b.C0378l;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: renamed from: b.j, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0376j implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0378l f2187a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0378l.a f2188b;

    public RunnableC0376j(C0378l.a aVar, C0378l c0378l) {
        this.f2188b = aVar;
        this.f2187a = c0378l;
    }

    @Override // java.lang.Runnable
    @SuppressLint({"DefaultLocale"})
    public void run() {
        C0378l.this.h.b(this.f2188b.e, this.f2188b.f, this.f2188b.h, this.f2188b.f2194a);
        Utils.notifyFailed(this.f2188b.f2196c, -13, String.format("Did not receive the expected response within %d seconds", Integer.valueOf(this.f2188b.g)));
    }
}

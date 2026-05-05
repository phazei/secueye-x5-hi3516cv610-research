package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: renamed from: a.a.a.a.b.a.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: SIGMeshBizRequest.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0323i implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Runnable f1263a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ SIGMeshBizRequest f1264b;

    public RunnableC0323i(SIGMeshBizRequest sIGMeshBizRequest, Runnable runnable) {
        this.f1264b = sIGMeshBizRequest;
        this.f1263a = runnable;
    }

    @Override // java.lang.Runnable
    public void run() {
        Runnable runnable = this.f1263a;
        if (runnable != null) {
            runnable.run();
        }
        Utils.notifyFailed(this.f1264b.f2804c, -13, "timeout");
    }
}

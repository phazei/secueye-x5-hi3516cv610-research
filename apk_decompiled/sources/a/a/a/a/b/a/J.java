package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;

/* JADX INFO: compiled from: SerialExecutionDispatcher.java */
/* JADX INFO: loaded from: classes.dex */
public class J implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K f1246a;

    public J(K k) {
        this.f1246a = k;
    }

    @Override // java.lang.Runnable
    public void run() {
        K k = this.f1246a;
        k.g = 0;
        k.f = Math.min(K.f1247b, k.e.size());
        for (int i = 0; i < K.f1247b && this.f1246a.e.size() > 0; i++) {
            SIGMeshBizRequest sIGMeshBizRequest = (SIGMeshBizRequest) this.f1246a.e.poll();
            if (sIGMeshBizRequest != null) {
                if (i != 0) {
                    try {
                        Thread.sleep(K.f1248c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.f1246a.a(sIGMeshBizRequest);
            }
        }
    }
}

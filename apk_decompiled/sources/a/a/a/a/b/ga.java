package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ga implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1329a;

    public ga(MeshService meshService) {
        this.f1329a = meshService;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1329a.mIsScanning) {
            return;
        }
        this.f1329a.startScan();
    }
}

package b;

import b.K;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class L implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ String f2120a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ K.d f2121b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ M f2122c;

    public L(M m, String str, K.d dVar) {
        this.f2122c = m;
        this.f2120a = str;
        this.f2121b = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2120a.equalsIgnoreCase(this.f2121b.f2119d.getAddress())) {
            this.f2121b.a(BleMeshManager.WriteReadType.WRITE);
        }
    }
}

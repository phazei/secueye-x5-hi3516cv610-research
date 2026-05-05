package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class Y implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1240a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1241b;

    public Y(MeshService meshService, ProvisionedMeshNode provisionedMeshNode) {
        this.f1241b = meshService;
        this.f1240a = provisionedMeshNode;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str = (String) this.f1241b.mAppKeyQueue.poll();
        Integer num = (Integer) this.f1241b.mAppKeyIndexQueue.poll();
        if (str != null) {
            if (num == null) {
                num = 0;
            }
            a.a.a.a.b.m.a.c(MeshService.TAG, "try to add app key: appKeyIndex = " + num + ", mAppKey = " + str);
            this.f1241b.mMeshManagerApi.a(this.f1240a, num.intValue(), str);
        }
        a.a.a.a.b.m.a.a(MeshService.TAG, "addAppKey");
    }
}

package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class fa implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1325a;

    public fa(MeshService meshService) {
        this.f1325a = meshService;
    }

    @Override // java.lang.Runnable
    public void run() {
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.TIMEOUT_ERROR;
        this.f1325a.handleProvisionFailed(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
    }
}

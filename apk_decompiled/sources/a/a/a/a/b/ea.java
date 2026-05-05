package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.ut.UtError;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ea implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshService f1322a;

    public ea(MeshService meshService) {
        this.f1322a = meshService;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (MeshDeviceInfoManager.getInstance().isLowCostDeviceExist()) {
            a.a.a.a.b.m.a.a(MeshService.TAG, "scan timeout");
            return;
        }
        if (this.f1322a.mIsScanning) {
            this.f1322a.sendBroadcastIsConnected(false, UtError.MESH_SCAN_TIMEOUT.getMsg());
        }
        this.f1322a.stopScan();
    }
}

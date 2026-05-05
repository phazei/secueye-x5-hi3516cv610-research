package a.a.a.a.b.g;

import a.a.a.a.b.G;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;

/* JADX INFO: compiled from: MeshDeviceInfoManager.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements MeshService.OnDisconnectListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ MeshDeviceInfoManager f1328a;

    public a(MeshDeviceInfoManager meshDeviceInfoManager) {
        this.f1328a = meshDeviceInfoManager;
    }

    @Override // com.alibaba.ailabs.iot.mesh.MeshService.OnDisconnectListener
    public void onDisconnected() {
        a.a.a.a.b.m.a.a(MeshDeviceInfoManager.TAG, "reconnect no lowPower");
        G.a().d().f();
    }
}

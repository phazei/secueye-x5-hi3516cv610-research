package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class ja implements MeshMsgListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ a.a.a.a.b.i.P f1463a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService.b f1464b;

    public ja(MeshService.b bVar, a.a.a.a.b.i.P p) {
        this.f1464b = bVar;
        this.f1463a = p;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener
    public void onReceiveMeshMessage(byte[] bArr, MeshAccessPayload meshAccessPayload) {
        this.f1463a.a(bArr, meshAccessPayload.getOpCode(), meshAccessPayload.getParameters());
    }
}

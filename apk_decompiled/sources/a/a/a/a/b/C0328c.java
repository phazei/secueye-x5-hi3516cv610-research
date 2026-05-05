package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: renamed from: a.a.a.a.b.c, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BleMeshHeartReportManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0328c implements IActionListener<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0329d f1303a;

    public C0328c(C0329d c0329d) {
        this.f1303a = c0329d;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.a("BleMeshHeartReportManager", "updateMeshNetworkParameters onSuccess() called with: result = [" + bool + "]");
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.a("BleMeshHeartReportManager", "updateMeshNetworkParameters onFailure() called with: errorCode = [" + i + "], desc = [" + str + "]");
    }
}

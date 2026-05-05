package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: compiled from: WiFiConfigOverMeshLogicController.java */
/* JADX INFO: loaded from: classes.dex */
public class L implements IActionListener<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ P f1359a;

    public L(P p) {
        this.f1359a = p;
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1359a.a(false, i, 0, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.callback.IActionListener
    public void onSuccess(Object obj) {
        this.f1359a.f1365c.postDelayed(this.f1359a.h = new K(this), 40000L);
    }
}

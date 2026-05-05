package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: WiFiConfigOverMeshLogicController.java */
/* JADX INFO: loaded from: classes.dex */
public class O implements WiFiConfigReplyParser.a<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ P f1362a;

    public O(P p) {
        this.f1362a = p;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser.a
    public void a(int i, int i2, String str) {
        this.f1362a.a(false, i, i2, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser.a
    public void a(WiFiConfigReplyParser.Status status) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        Utils.notifyFailed(this.f1362a.f, i, str);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(Object obj) {
        this.f1362a.a();
        Utils.notifySuccess((IActionListener<String>) this.f1362a.f, "");
    }
}

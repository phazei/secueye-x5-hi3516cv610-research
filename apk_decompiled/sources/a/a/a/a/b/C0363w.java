package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser;

/* JADX INFO: renamed from: a.a.a.a.b.w, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C0363w implements WiFiConfigReplyParser.a<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1553a;

    public C0363w(DeviceProvisioningWorker deviceProvisioningWorker) {
        this.f1553a = deviceProvisioningWorker;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser.a
    public void a(int i, int i2, String str) {
        this.f1553a.a(false, i, i2, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser.a
    public void a(WiFiConfigReplyParser.Status status) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onSuccess(Object obj) {
        this.f1553a.a(true, 0, 0, "");
    }
}

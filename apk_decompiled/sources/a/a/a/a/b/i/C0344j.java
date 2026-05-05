package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;

/* JADX INFO: renamed from: a.a.a.a.b.i.j, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0344j implements IActionListener<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1425a;

    public C0344j(FastProvisionManager fastProvisionManager) {
        this.f1425a = fastProvisionManager;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(byte[] bArr) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "Advertising network data, begin scan");
        FastProvisionManager fastProvisionManager = this.f1425a;
        fastProvisionManager.startScanDeviceAdvertise(fastProvisionManager.appContext);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.IActionListener
    public void onFailure(int i, String str) {
        this.f1425a.onProvisionFailed(-1, "failed to advertise network data");
    }
}

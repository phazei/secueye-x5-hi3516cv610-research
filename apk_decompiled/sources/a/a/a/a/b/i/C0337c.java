package a.a.a.a.b.i;

import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.i.c, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0337c implements BleAdvertiseCallback<Boolean> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BaseMeshNode f1379a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1380b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1381c;

    public C0337c(FastProvisionManager fastProvisionManager, BaseMeshNode baseMeshNode, byte[] bArr) {
        this.f1381c = fastProvisionManager;
        this.f1379a = baseMeshNode;
        this.f1380b = bArr;
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onSuccess(Boolean bool) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "send control msg success");
        this.f1381c.transportCallback.onFastProvisionDataSend(this.f1379a, this.f1380b);
        BaseMeshNode baseMeshNode = this.f1379a;
        if (baseMeshNode instanceof UnprovisionedMeshNode) {
            this.f1381c.unprovisionedMeshNode = (UnprovisionedMeshNode) baseMeshNode;
        }
        FastProvisionManager fastProvisionManager = this.f1381c;
        fastProvisionManager.startScanDeviceAdvertise(fastProvisionManager.appContext);
    }

    @Override // com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback
    public void onFailure(int i, String str) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "send control msg failed, errorCode: " + i + ", desc: " + str);
    }
}

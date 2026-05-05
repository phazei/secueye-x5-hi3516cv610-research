package a.a.a.a.b.i;

import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: renamed from: a.a.a.a.b.i.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0343i implements InterfaceC0367a.InterfaceC0176a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1424a;

    public C0343i(FastProvisionManager fastProvisionManager) {
        this.f1424a = fastProvisionManager;
    }

    @Override // b.InterfaceC0367a.InterfaceC0176a
    public void a(UnprovisionedMeshNode unprovisionedMeshNode, boolean z) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "CheckConfirmationValueMatchesCallback: match = " + z + ", mac = " + unprovisionedMeshNode.getBluetoothDeviceAddress() + ", origin mac " + this.f1424a.unprovisionedMeshNodeData.getDeviceMac());
        if (z) {
            FastProvisionManager fastProvisionManager = this.f1424a;
            fastProvisionManager.onReceiveVerifyResultFromCloud(fastProvisionManager.unprovisionedMeshNodeData);
            return;
        }
        this.f1424a.onProvisionFailed(-1, "auth confirmation failed: match = " + z + ", callback device: " + unprovisionedMeshNode.getBluetoothDeviceAddress() + ", origin device: " + this.f1424a.unprovisionedMeshNodeData.getDeviceMac());
    }
}

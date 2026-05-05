package a.a.a.a.b.i;

import b.InterfaceC0367a;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class C implements InterfaceC0367a.InterfaceC0176a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ J f1345a;

    public C(J j) {
        this.f1345a = j;
    }

    @Override // b.InterfaceC0367a.InterfaceC0176a
    public void a(UnprovisionedMeshNode unprovisionedMeshNode, boolean z) {
        a.a.a.a.b.m.a.c(this.f1345a.f1354a, "CheckConfirmationValueMatchesCallback: match = " + z + ", mac = " + unprovisionedMeshNode.getBluetoothDeviceAddress() + ", origin mac " + this.f1345a.i.getDeviceMac());
        if (z) {
            J j = this.f1345a;
            j.onReceiveVerifyResultFromCloud(j.i);
            return;
        }
        this.f1345a.onProvisionFailed(-1, "auth confirmation failed: match = " + z + ", callback device: " + unprovisionedMeshNode.getBluetoothDeviceAddress() + ", origin device: " + this.f1345a.i.getDeviceMac());
    }
}

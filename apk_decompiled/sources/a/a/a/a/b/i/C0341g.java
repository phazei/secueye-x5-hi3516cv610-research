package a.a.a.a.b.i;

import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: renamed from: a.a.a.a.b.i.g, reason: case insensitive filesystem */
/* JADX INFO: compiled from: FastProvisionManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0341g implements InterfaceC0367a.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedMeshNodeData f1420a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ FastProvisionManager f1421b;

    public C0341g(FastProvisionManager fastProvisionManager, UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.f1421b = fastProvisionManager;
        this.f1420a = unprovisionedMeshNodeData;
    }

    @Override // b.InterfaceC0367a.b
    public void generate(String str) {
        a.a.a.a.b.m.a.c(FastProvisionManager.TAG, "receive confirmationCloud: " + str);
        this.f1421b.onReceiveConfirmationFromCloud(this.f1420a, str);
    }
}

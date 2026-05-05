package b;

import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: renamed from: b.a, reason: case insensitive filesystem */
/* JADX INFO: compiled from: CloudComfirmationProvisioningCallbacks.java */
/* JADX INFO: loaded from: classes.dex */
public interface InterfaceC0367a {

    /* JADX INFO: renamed from: b.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: CloudComfirmationProvisioningCallbacks.java */
    public interface InterfaceC0176a {
        void a(UnprovisionedMeshNode unprovisionedMeshNode, boolean z);
    }

    /* JADX INFO: renamed from: b.a$b */
    /* JADX INFO: compiled from: CloudComfirmationProvisioningCallbacks.java */
    public interface b {
        void generate(String str);
    }

    void checkConfirmationValueMatches(UnprovisionedMeshNode unprovisionedMeshNode, UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr, byte[] bArr2, byte[] bArr3, InterfaceC0176a interfaceC0176a);

    void generateConfirmationValue(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr, byte[] bArr2, b bVar);
}

package b;

import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: b.d, reason: case insensitive filesystem */
/* JADX INFO: compiled from: InternalTransportCallbacks.java */
/* JADX INFO: loaded from: classes.dex */
public interface InterfaceC0370d {
    void a(ProvisionedMeshNode provisionedMeshNode);

    void b(ProvisionedMeshNode provisionedMeshNode);

    void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr);
}

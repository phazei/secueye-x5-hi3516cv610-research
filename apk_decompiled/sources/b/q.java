package b;

import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: MeshStatusCallbacks.java */
/* JADX INFO: loaded from: classes.dex */
public interface q {
    ProvisionedMeshNode getMeshNode(byte[] bArr, byte[] bArr2);

    void onAppKeyAddSent(ProvisionedMeshNode provisionedMeshNode);

    void onAppKeyBindSent(ProvisionedMeshNode provisionedMeshNode);

    void onAppKeyBindStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, int i2, int i3, int i4);

    void onAppKeyStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, int i2, int i3);

    void onAppKeyUnbindSent(ProvisionedMeshNode provisionedMeshNode);

    void onBlockAcknowledgementReceived(ProvisionedMeshNode provisionedMeshNode);

    void onBlockAcknowledgementSent(ProvisionedMeshNode provisionedMeshNode);

    void onCommonMessageStatusReceived(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, String str, byte[] bArr2, a.a.a.a.b.h.a aVar);

    void onCompositionDataStatusReceived(ProvisionedMeshNode provisionedMeshNode);

    void onGenericLevelGetSent(ProvisionedMeshNode provisionedMeshNode);

    void onGenericLevelSetUnacknowledgedSent(ProvisionedMeshNode provisionedMeshNode);

    void onGenericLevelStatusReceived(ProvisionedMeshNode provisionedMeshNode, int i, int i2, int i3, int i4);

    void onGenericOnOffGetSent(ProvisionedMeshNode provisionedMeshNode);

    void onGenericOnOffSetUnacknowledgedSent(ProvisionedMeshNode provisionedMeshNode);

    void onGenericOnOffStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, Boolean bool, int i, int i2);

    void onGetCompositionDataSent(ProvisionedMeshNode provisionedMeshNode);

    void onMeshNodeResetSent(ProvisionedMeshNode provisionedMeshNode);

    void onMeshNodeResetStatusReceived(ProvisionedMeshNode provisionedMeshNode);

    void onPublicationSetSent(ProvisionedMeshNode provisionedMeshNode);

    void onPublicationStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, byte[] bArr, byte[] bArr2, int i2);

    void onSubscriptionAddSent(ProvisionedMeshNode provisionedMeshNode);

    void onSubscriptionDeleteSent(ProvisionedMeshNode provisionedMeshNode);

    void onSubscriptionStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, byte[] bArr, byte[] bArr2, int i2);

    void onTransactionFailed(ProvisionedMeshNode provisionedMeshNode, int i, boolean z);

    void onUnknownPduReceived(ProvisionedMeshNode provisionedMeshNode);

    void onVendorModelMessageStatusReceived(ProvisionedMeshNode provisionedMeshNode, byte[] bArr);
}

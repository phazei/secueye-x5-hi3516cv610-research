package meshprovisioner.states;

import meshprovisioner.states.ProvisioningState;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningCompleteState extends ProvisioningState {
    public final UnprovisionedMeshNode unprovisionedMeshNode;

    public ProvisioningCompleteState(UnprovisionedMeshNode unprovisionedMeshNode) {
        this.unprovisionedMeshNode = unprovisionedMeshNode;
        unprovisionedMeshNode.setIsProvisioned(true);
        unprovisionedMeshNode.setProvisionedTime(System.currentTimeMillis());
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISINING_COMPLETE;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        return true;
    }
}

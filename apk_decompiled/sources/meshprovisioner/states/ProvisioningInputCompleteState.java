package meshprovisioner.states;

import b.InterfaceC0370d;
import b.p;
import meshprovisioner.states.ProvisioningState;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningInputCompleteState extends ProvisioningState {
    public final InterfaceC0370d mInternalTransportCallbacks;
    public final p mMeshProvisioningStatusCallbacks;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;

    public ProvisioningInputCompleteState(UnprovisionedMeshNode unprovisionedMeshNode, InterfaceC0370d interfaceC0370d, p pVar) {
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
        this.mInternalTransportCallbacks = interfaceC0370d;
        this.mMeshProvisioningStatusCallbacks = pVar;
    }

    private byte[] createProvisioningInputComplete() {
        return new byte[]{3, 4};
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
        this.mMeshProvisioningStatusCallbacks.onProvisioningInputCompleteSent(this.mUnprovisionedMeshNode);
        this.mInternalTransportCallbacks.sendPdu(this.mUnprovisionedMeshNode, createProvisioningInputComplete());
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISINING_INPUT_COMPLETE;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        return true;
    }
}

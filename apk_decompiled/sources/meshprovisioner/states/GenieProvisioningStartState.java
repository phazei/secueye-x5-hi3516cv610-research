package meshprovisioner.states;

import a.a.a.a.b.m.a;
import b.InterfaceC0370d;
import b.p;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.ParseOutputOOBActions;
import meshprovisioner.utils.ParseProvisioningAlgorithm;

/* JADX INFO: loaded from: classes4.dex */
public class GenieProvisioningStartState extends ProvisioningState {
    public int inputOOBAction;
    public final InterfaceC0370d mInternalTransportCallbacks;
    public final p mMeshProvisioningStatusCallbacks;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;
    public final String TAG = GenieProvisioningStartState.class.getSimpleName();
    public int numberOfElements = 1;
    public int algorithm = 1;
    public int publicKeyType = 0;
    public int staticOOBType = 1;
    public int outputOOBSize = 0;
    public int outputOOBAction = 0;
    public int inputOOBSize = 0;

    public GenieProvisioningStartState(UnprovisionedMeshNode unprovisionedMeshNode, InterfaceC0370d interfaceC0370d, p pVar) {
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
        this.mInternalTransportCallbacks = interfaceC0370d;
        this.mMeshProvisioningStatusCallbacks = pVar;
    }

    private byte[] createProvisioningStartPDU() {
        byte[] bArr = new byte[7];
        bArr[0] = 3;
        bArr[1] = 2;
        bArr[2] = ParseProvisioningAlgorithm.getAlgorithmValue(this.algorithm);
        bArr[3] = 0;
        short sSelectOutputActionsFromBitMask = (byte) ParseOutputOOBActions.selectOutputActionsFromBitMask(this.outputOOBAction);
        if (this.staticOOBType != 0) {
            bArr[4] = 1;
            bArr[5] = 0;
            bArr[6] = 0;
        } else if (sSelectOutputActionsFromBitMask != 0) {
            bArr[4] = 2;
            bArr[5] = (byte) ParseOutputOOBActions.getOuputOOBActionValue(sSelectOutputActionsFromBitMask);
            bArr[6] = (byte) this.outputOOBSize;
        } else {
            bArr[4] = 0;
            bArr[5] = 0;
            bArr[6] = 0;
        }
        a.a(this.TAG, "Provisioning start PDU: " + MeshParserUtils.bytesToHex(bArr, true));
        return bArr;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
        byte[] bArrCreateProvisioningStartPDU = createProvisioningStartPDU();
        this.mMeshProvisioningStatusCallbacks.onProvisioningStartSent(this.mUnprovisionedMeshNode);
        this.mInternalTransportCallbacks.sendPdu(this.mUnprovisionedMeshNode, bArrCreateProvisioningStartPDU);
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISIONING_START;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        return true;
    }

    public void setProvisioningCapabilities(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.numberOfElements = i;
        this.algorithm = i2;
        this.publicKeyType = i3;
        this.staticOOBType = i4;
        this.outputOOBSize = i5;
        this.outputOOBAction = i6;
        this.inputOOBSize = i7;
        this.inputOOBAction = i8;
    }
}

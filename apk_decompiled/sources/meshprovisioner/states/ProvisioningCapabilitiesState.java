package meshprovisioner.states;

import a.a.a.a.b.m.a;
import b.p;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.utils.AlgorithmInformationParser;
import meshprovisioner.utils.ParseInputOOBActions;
import meshprovisioner.utils.ParseOutputOOBActions;
import meshprovisioner.utils.ParsePublicKeyInformation;
import meshprovisioner.utils.ParseStaticOutputOOBInformation;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningCapabilitiesState extends ProvisioningState {
    public static final String TAG = ProvisioningInviteState.class.getSimpleName();
    public ProvisioningCapabilities capabilities;
    public final p mCallbacks;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;

    public ProvisioningCapabilitiesState(UnprovisionedMeshNode unprovisionedMeshNode, p pVar) {
        this.mCallbacks = pVar;
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
    }

    private boolean parseProvisioningCapabilities(byte[] bArr) {
        ProvisioningCapabilities provisioningCapabilities = new ProvisioningCapabilities();
        provisioningCapabilities.setRawCapabilities(bArr);
        if (bArr[2] == 0) {
            throw new IllegalArgumentException("Number of elements cannot be zero");
        }
        byte b2 = bArr[2];
        a.a(TAG, "Number of elements: " + ((int) b2));
        provisioningCapabilities.setNumberOfElements(b2);
        short s = (short) (((bArr[3] & 255) << 8) | (bArr[4] & 255));
        a.a(TAG, "Algorithm: " + AlgorithmInformationParser.parseAlgorithm(s));
        provisioningCapabilities.setSupportedAlgorithm(s);
        byte b3 = bArr[5];
        a.a(TAG, "Public key type: " + ParsePublicKeyInformation.parsePublicKeyInformation(b3));
        provisioningCapabilities.setPublicKeyType(b3);
        byte b4 = bArr[6];
        a.a(TAG, "Static OOB type: " + ParseStaticOutputOOBInformation.parseStaticOOBActionInformation(b4));
        provisioningCapabilities.setStaticOOBType(b4);
        byte b5 = bArr[7];
        a.a(TAG, "Output OOB size: " + ((int) b5));
        provisioningCapabilities.setOutputOOBSize(b5);
        short s2 = (short) (((bArr[8] & 255) << 8) | (bArr[9] & 255));
        ParseOutputOOBActions.parseOutputActionsFromBitMask(s2);
        provisioningCapabilities.setOutputOOBAction(s2);
        byte b6 = bArr[10];
        a.a(TAG, "Input OOB size: " + ((int) b6));
        provisioningCapabilities.setInputOOBSize(b6);
        short s3 = (short) ((bArr[12] & 255) | ((bArr[11] & 255) << 8));
        ParseInputOOBActions.parseInputActionsFromBitMask(s3);
        provisioningCapabilities.setInputOOBAction(s3);
        this.capabilities = provisioningCapabilities;
        return true;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
    }

    public ProvisioningCapabilities getCapabilities() {
        return this.capabilities;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISIONING_CAPABILITIES;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        boolean provisioningCapabilities = parseProvisioningCapabilities(bArr);
        this.mUnprovisionedMeshNode.setProvisioningCapabilities(this.capabilities);
        this.mCallbacks.onProvisioningCapabilitiesReceived(this.mUnprovisionedMeshNode);
        return provisioningCapabilities;
    }
}

package meshprovisioner.states;

import a.a.a.a.b.m.a;
import b.InterfaceC0367a;
import b.InterfaceC0370d;
import b.o;
import b.p;
import java.nio.ByteBuffer;
import java.util.Arrays;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningRandomConfirmationState extends ProvisioningState {
    public final String TAG = ProvisioningRandomConfirmationState.class.getSimpleName();
    public final InterfaceC0370d mInternalTransportCallbacks;
    public final p mMeshProvisioningStatusCallbacks;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;
    public final o pduHandler;

    public ProvisioningRandomConfirmationState(o oVar, UnprovisionedMeshNode unprovisionedMeshNode, InterfaceC0370d interfaceC0370d, p pVar) {
        this.pduHandler = oVar;
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
        this.mInternalTransportCallbacks = interfaceC0370d;
        this.mMeshProvisioningStatusCallbacks = pVar;
    }

    private byte[] createProvisionerRandomPDU() {
        byte[] provisionerRandom = this.mUnprovisionedMeshNode.getProvisionerRandom();
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(provisionerRandom.length + 2);
        byteBufferAllocate.put(new byte[]{3, 6});
        byteBufferAllocate.put(provisionerRandom);
        byte[] bArrArray = byteBufferAllocate.array();
        a.a(this.TAG, "Provisioner random PDU: " + MeshParserUtils.bytesToHex(bArrArray, false));
        return bArrArray;
    }

    private void parseProvisioneeRandom(byte[] bArr) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArr.length - 2);
        byteBufferAllocate.put(bArr, 2, byteBufferAllocate.limit());
        this.mUnprovisionedMeshNode.setProvisioneeRandom(byteBufferAllocate.array());
    }

    private void provisioneeMatches() {
        byte[] provisioneeRandom = this.mUnprovisionedMeshNode.getProvisioneeRandom();
        byte[] bArrA = this.pduHandler.a(this.mUnprovisionedMeshNode.getProvisionerPublicKeyXY(), this.mUnprovisionedMeshNode.getProvisioneePublicKeyXY());
        a.a(this.TAG, "Confirmation inputs: " + MeshParserUtils.bytesToHex(bArrA, false));
        byte[] bArrCalculateSalt = SecureUtils.calculateSalt(bArrA);
        a.a(this.TAG, "Confirmation salt: " + MeshParserUtils.bytesToHex(bArrCalculateSalt, false));
        byte[] bArrCalculateK1 = SecureUtils.calculateK1(this.mUnprovisionedMeshNode.getSharedECDHSecret(), bArrCalculateSalt, SecureUtils.PRCK);
        a.a(this.TAG, "Confirmation key: " + MeshParserUtils.bytesToHex(bArrCalculateK1, false));
        if (this.mUnprovisionedMeshNode.useCloudeConfirmationProvisioning()) {
            InterfaceC0367a cloudComfirmationProvisioningCallbacks = this.mUnprovisionedMeshNode.getCloudComfirmationProvisioningCallbacks();
            UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(this.mUnprovisionedMeshNode.getServiceData());
            UnprovisionedMeshNode unprovisionedMeshNode = this.mUnprovisionedMeshNode;
            cloudComfirmationProvisioningCallbacks.checkConfirmationValueMatches(unprovisionedMeshNode, unprovisionedMeshNodeData, unprovisionedMeshNode.getProvisioneeConfirmation(), bArrCalculateK1, provisioneeRandom, this.pduHandler);
            a.a(this.TAG, "Confirmation values match checked by cloud!!!!");
            return;
        }
        byte[] authenticationValue = this.mUnprovisionedMeshNode.getAuthenticationValue();
        a.a(this.TAG, "Authentication value: " + MeshParserUtils.bytesToHex(authenticationValue, false));
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(provisioneeRandom.length + authenticationValue.length);
        byteBufferAllocate.put(provisioneeRandom);
        byteBufferAllocate.put(authenticationValue);
        byte[] bArrCalculateCMAC = SecureUtils.calculateCMAC(byteBufferAllocate.array(), bArrCalculateK1);
        if (!Arrays.equals(bArrCalculateCMAC, this.mUnprovisionedMeshNode.getProvisioneeConfirmation())) {
            this.pduHandler.a(this.mUnprovisionedMeshNode, false);
            return;
        }
        a.a(this.TAG, "Confirmation values match!!!!: " + MeshParserUtils.bytesToHex(bArrCalculateCMAC, false));
        this.pduHandler.a(this.mUnprovisionedMeshNode, true);
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
        byte[] bArrCreateProvisionerRandomPDU = createProvisionerRandomPDU();
        this.mMeshProvisioningStatusCallbacks.onProvisioningRandomSent(this.mUnprovisionedMeshNode);
        this.mInternalTransportCallbacks.sendPdu(this.mUnprovisionedMeshNode, bArrCreateProvisionerRandomPDU);
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISINING_RANDOM;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        this.mMeshProvisioningStatusCallbacks.onProvisioningRandomReceived(this.mUnprovisionedMeshNode);
        parseProvisioneeRandom(bArr);
        provisioneeMatches();
        return true;
    }
}

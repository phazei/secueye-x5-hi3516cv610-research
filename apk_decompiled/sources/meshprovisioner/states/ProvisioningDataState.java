package meshprovisioner.states;

import a.a.a.a.b.m.a;
import b.InterfaceC0370d;
import b.o;
import b.p;
import java.nio.ByteBuffer;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningDataState extends ProvisioningState {
    public final String TAG = ProvisioningRandomConfirmationState.class.getSimpleName();
    public final InterfaceC0370d mInternalTransportCallbacks;
    public final p mMeshProvisioningStatusCallbacks;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;
    public final o pduHandler;

    public ProvisioningDataState(o oVar, UnprovisionedMeshNode unprovisionedMeshNode, InterfaceC0370d interfaceC0370d, p pVar) {
        this.pduHandler = oVar;
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
        this.mInternalTransportCallbacks = interfaceC0370d;
        this.mMeshProvisioningStatusCallbacks = pVar;
    }

    private byte[] createProvisioningDataPDU() {
        byte[] bArrGenerateProvisioningSalt = generateProvisioningSalt();
        a.a(this.TAG, "Provisioning salt: " + MeshParserUtils.bytesToHex(bArrGenerateProvisioningSalt, false));
        byte[] sharedECDHSecret = this.mUnprovisionedMeshNode.getSharedECDHSecret();
        byte[] bArrCalculateCMAC = SecureUtils.calculateCMAC(sharedECDHSecret, bArrGenerateProvisioningSalt);
        byte[] bArrCalculateCMAC2 = SecureUtils.calculateCMAC(SecureUtils.PRSK, bArrCalculateCMAC);
        a.a(this.TAG, "Session key: " + MeshParserUtils.bytesToHex(bArrCalculateCMAC2, false));
        byte[] bArrGenerateSessionNonce = generateSessionNonce(sharedECDHSecret, bArrGenerateProvisioningSalt);
        a.a(this.TAG, "Session nonce: " + MeshParserUtils.bytesToHex(bArrGenerateSessionNonce, false));
        byte[] bArrCalculateCMAC3 = SecureUtils.calculateCMAC(SecureUtils.PRDK, bArrCalculateCMAC);
        a.a(this.TAG, "Device key: " + MeshParserUtils.bytesToHex(bArrCalculateCMAC3, false));
        this.mUnprovisionedMeshNode.setDeviceKey(bArrCalculateCMAC3);
        byte[] networkKey = this.mUnprovisionedMeshNode.getNetworkKey();
        a.a(this.TAG, "Network key: " + MeshParserUtils.bytesToHex(networkKey, false));
        byte[] keyIndex = this.mUnprovisionedMeshNode.getKeyIndex();
        a.a(this.TAG, "Key index: " + MeshParserUtils.bytesToHex(keyIndex, false));
        byte[] flags = this.mUnprovisionedMeshNode.getFlags();
        a.a(this.TAG, "Flags: " + MeshParserUtils.bytesToHex(flags, false));
        byte[] ivIndex = this.mUnprovisionedMeshNode.getIvIndex();
        a.a(this.TAG, "IV index: " + MeshParserUtils.bytesToHex(ivIndex, false));
        byte[] unicastAddress = this.mUnprovisionedMeshNode.getUnicastAddress();
        a.a(this.TAG, "Unicast address: " + MeshParserUtils.bytesToHex(unicastAddress, false));
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(networkKey.length + keyIndex.length + flags.length + ivIndex.length + unicastAddress.length);
        byteBufferAllocate.put(networkKey);
        byteBufferAllocate.put(keyIndex);
        byteBufferAllocate.put(flags);
        byteBufferAllocate.put(ivIndex);
        byteBufferAllocate.put(unicastAddress);
        byte[] bArrArray = byteBufferAllocate.array();
        a.a(this.TAG, "Provisioning data: " + MeshParserUtils.bytesToHex(bArrArray, false));
        byte[] bArrEncryptCCM = SecureUtils.encryptCCM(bArrArray, bArrCalculateCMAC2, bArrGenerateSessionNonce, 8);
        a.a(this.TAG, "Encrypted provisioning data: " + MeshParserUtils.bytesToHex(bArrEncryptCCM, false));
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(bArrEncryptCCM.length + 2);
        byteBufferAllocate2.put((byte) 3);
        byteBufferAllocate2.put((byte) 7);
        byteBufferAllocate2.put(bArrEncryptCCM);
        byte[] bArrArray2 = byteBufferAllocate2.array();
        a.a(this.TAG, "Prov Data: " + MeshParserUtils.bytesToHex(bArrArray2, false));
        return bArrArray2;
    }

    private byte[] generateProvisioningSalt() {
        byte[] bArrCalculateSalt = SecureUtils.calculateSalt(this.pduHandler.a(this.mUnprovisionedMeshNode.getProvisionerPublicKeyXY(), this.mUnprovisionedMeshNode.getProvisioneePublicKeyXY()));
        byte[] provisionerRandom = this.mUnprovisionedMeshNode.getProvisionerRandom();
        byte[] provisioneeRandom = this.mUnprovisionedMeshNode.getProvisioneeRandom();
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArrCalculateSalt.length + provisionerRandom.length + provisioneeRandom.length);
        byteBufferAllocate.put(bArrCalculateSalt);
        byteBufferAllocate.put(provisionerRandom);
        byteBufferAllocate.put(provisioneeRandom);
        return SecureUtils.calculateSalt(byteBufferAllocate.array());
    }

    private byte[] generateSessionNonce(byte[] bArr, byte[] bArr2) {
        byte[] bArrCalculateK1 = SecureUtils.calculateK1(bArr, bArr2, SecureUtils.PRSN);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArrCalculateK1.length - 3);
        byteBufferAllocate.put(bArrCalculateK1, 3, byteBufferAllocate.limit());
        return byteBufferAllocate.array();
    }

    private void sendProvisioningData() {
        byte[] bArrCreateProvisioningDataPDU = createProvisioningDataPDU();
        this.mMeshProvisioningStatusCallbacks.onProvisioningDataSent(this.mUnprovisionedMeshNode);
        this.mInternalTransportCallbacks.sendPdu(this.mUnprovisionedMeshNode, bArrCreateProvisioningDataPDU);
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
        sendProvisioningData();
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISINING_DATA;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        return true;
    }
}

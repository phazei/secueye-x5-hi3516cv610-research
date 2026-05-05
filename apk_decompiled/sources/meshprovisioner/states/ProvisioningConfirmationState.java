package meshprovisioner.states;

import a.a.a.a.b.m.a;
import android.text.TextUtils;
import b.InterfaceC0367a;
import b.InterfaceC0370d;
import b.o;
import b.p;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningConfirmationState extends ProvisioningState {
    public final String TAG = ProvisioningConfirmationState.class.getSimpleName();
    public final InterfaceC0370d mInternalTransportCallbacks;
    public final p mMeshProvisioningStatusCallbacks;
    public byte[] mProvisioningConfirmationPDU;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;
    public final o pduHandler;
    public String pin;

    public ProvisioningConfirmationState(o oVar, UnprovisionedMeshNode unprovisionedMeshNode, InterfaceC0370d interfaceC0370d, p pVar) {
        this.pduHandler = oVar;
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
        this.mInternalTransportCallbacks = interfaceC0370d;
        this.mMeshProvisioningStatusCallbacks = pVar;
    }

    private void createProvisioningConfirmation(byte[] bArr) {
        byte[] bArrA = this.pduHandler.a(this.mUnprovisionedMeshNode.getProvisionerPublicKeyXY(), this.mUnprovisionedMeshNode.getProvisioneePublicKeyXY());
        a.a(this.TAG, "Confirmation inputs: " + MeshParserUtils.bytesToHex(bArrA, false));
        byte[] bArrCalculateSalt = SecureUtils.calculateSalt(bArrA);
        a.a(this.TAG, "Confirmation salt: " + MeshParserUtils.bytesToHex(bArrCalculateSalt, false));
        byte[] bArrCalculateK1 = SecureUtils.calculateK1(this.mUnprovisionedMeshNode.getSharedECDHSecret(), bArrCalculateSalt, SecureUtils.PRCK);
        a.a(this.TAG, "Confirmation key: " + MeshParserUtils.bytesToHex(bArrCalculateK1, false));
        byte[] bArrGenerateRandomNumber = SecureUtils.generateRandomNumber();
        this.mUnprovisionedMeshNode.setProvisionerRandom(bArrGenerateRandomNumber);
        a.a(this.TAG, "Provisioner random: " + MeshParserUtils.bytesToHex(bArrGenerateRandomNumber, false));
        if (this.mUnprovisionedMeshNode.useCloudeConfirmationProvisioning()) {
            this.mUnprovisionedMeshNode.getCloudComfirmationProvisioningCallbacks().generateConfirmationValue(new UnprovisionedMeshNodeData(this.mUnprovisionedMeshNode.getServiceData()), bArrCalculateK1, bArrGenerateRandomNumber, new InterfaceC0367a.b() { // from class: meshprovisioner.states.ProvisioningConfirmationState.1
                @Override // b.InterfaceC0367a.b
                public void generate(String str) {
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    a.a(ProvisioningConfirmationState.this.TAG, "Provisioning confirmation: " + str);
                    ProvisioningConfirmationState.this.mProvisioningConfirmationPDU = MeshParserUtils.toByteArray(str);
                    ProvisioningConfirmationState.this.mMeshProvisioningStatusCallbacks.onProvisioningConfirmationSent(ProvisioningConfirmationState.this.mUnprovisionedMeshNode);
                    ProvisioningConfirmationState.this.mInternalTransportCallbacks.sendPdu(ProvisioningConfirmationState.this.mUnprovisionedMeshNode, ProvisioningConfirmationState.this.mProvisioningConfirmationPDU);
                }
            });
            return;
        }
        byte[] bArrGenerateAuthenticationValue = generateAuthenticationValue(bArr);
        this.mUnprovisionedMeshNode.setAuthenticationValue(bArrGenerateAuthenticationValue);
        a.a(this.TAG, "Authentication value: " + MeshParserUtils.bytesToHex(bArrGenerateAuthenticationValue, false));
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArrGenerateRandomNumber.length + bArrGenerateAuthenticationValue.length);
        byteBufferAllocate.put(bArrGenerateRandomNumber);
        byteBufferAllocate.put(bArrGenerateAuthenticationValue);
        byte[] bArrCalculateCMAC = SecureUtils.calculateCMAC(byteBufferAllocate.array(), bArrCalculateK1);
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(bArrCalculateCMAC.length + 2);
        byteBufferAllocate2.put(new byte[]{3, 5});
        byteBufferAllocate2.put(bArrCalculateCMAC);
        this.mProvisioningConfirmationPDU = byteBufferAllocate2.array();
        a.a(this.TAG, "Provisioning confirmation: " + MeshParserUtils.bytesToHex(this.mProvisioningConfirmationPDU, false));
        this.mMeshProvisioningStatusCallbacks.onProvisioningConfirmationSent(this.mUnprovisionedMeshNode);
        this.mInternalTransportCallbacks.sendPdu(this.mUnprovisionedMeshNode, this.mProvisioningConfirmationPDU);
    }

    private byte[] generateAuthenticationValue(byte[] bArr) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(16);
        if (bArr != null) {
            Integer numValueOf = Integer.valueOf(new String(bArr, Charset.forName("UTF-8")));
            byteBufferAllocate.position(12);
            byteBufferAllocate.putInt(numValueOf.intValue());
        }
        return byteBufferAllocate.array();
    }

    private void parseProvisioneeConfirmation(byte[] bArr) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArr.length - 2);
        byteBufferAllocate.put(bArr, 2, byteBufferAllocate.limit());
        this.mUnprovisionedMeshNode.setProvisioneeConfirmation(byteBufferAllocate.array());
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
        if (TextUtils.isEmpty(this.pin)) {
            createProvisioningConfirmation(null);
        } else {
            createProvisioningConfirmation(this.pin.getBytes());
        }
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISIONING_CONFIRMATION;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        this.mMeshProvisioningStatusCallbacks.onProvisioningConfirmationReceived(this.mUnprovisionedMeshNode);
        parseProvisioneeConfirmation(bArr);
        return true;
    }

    public void setPin(String str) {
        this.pin = str;
    }
}

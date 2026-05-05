package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.models.VendorModel;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class VendorModelMessage extends VendorModelMessageState {
    public static final String TAG = "VendorModelMessage";
    public static final int VENDOR_MODEL_OPCODE_LENGTH = 4;
    public final byte[] dstAddress;
    public final int mAszmic;
    public final int opCode;
    public final byte[] parameters;

    public VendorModelMessage(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, boolean z, byte[] bArr, int i, int i2, byte[] bArr2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mMeshModel = meshModel;
        this.mAszmic = z ? 1 : 0;
        this.dstAddress = bArr;
        this.mAppKeyIndex = i;
        this.opCode = i2;
        this.parameters = bArr2;
        createAccessMessage();
    }

    private void createAccessMessage() {
        byte[] byteArray = MeshParserUtils.toByteArray(this.mMeshModel.getBoundAppkeys().get(Integer.valueOf(this.mAppKeyIndex)));
        this.message = this.mMeshTransport.createVendorMeshMessage(this.mProvisionedMeshNode, (VendorModel) this.mMeshModel, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public void executeSend() {
        a.a(TAG, "Sending acknowledged vendor model message");
        super.executeSend();
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return null;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a(TAG, "Message reassembly may not be complete yet");
        } else {
            if (!(pdu instanceof b.d.a)) {
                parseControlMessage((b) pdu, this.mPayloads.size());
                return true;
            }
            byte[] bArrU = ((b.d.a) pdu).u();
            a.a(TAG, "Unexpected access message received: " + MeshParserUtils.bytesToHex(bArrU, false));
        }
        return false;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}

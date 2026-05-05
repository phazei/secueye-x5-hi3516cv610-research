package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import b.q;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class GenericOnOffGet extends GenericMessageState {
    public static final String TAG = "GenericOnOffGet";
    public final byte[] dstAddress;
    public final int mAszmic;

    public GenericOnOffGet(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, boolean z, byte[] bArr, int i) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mAszmic = z ? 1 : 0;
        this.dstAddress = bArr;
        this.mMeshModel = meshModel;
        this.mAppKeyIndex = i;
        createAccessMessage();
    }

    private void createAccessMessage() {
        byte[] byteArray = MeshParserUtils.toByteArray(this.mMeshModel.getBoundAppkeys().get(Integer.valueOf(this.mAppKeyIndex)));
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, 33281, null);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public void executeSend() {
        q qVar;
        a.a(TAG, "Sending Generic OnOff get");
        super.executeSend();
        if (this.mPayloads.isEmpty() || (qVar = this.mMeshStatusCallbacks) == null) {
            return;
        }
        qVar.onGenericOnOffGetSent(this.mProvisionedMeshNode);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.GENERIC_ON_OFF_GET_STATE;
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

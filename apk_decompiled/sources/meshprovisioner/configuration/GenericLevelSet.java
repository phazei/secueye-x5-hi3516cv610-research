package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import b.f.f;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLevelSet extends GenericMessageState implements f {
    public static final int GENERIC_LEVEL_SET_PARAMS_LENGTH = 3;
    public static final int GENERIC_LEVEL_SET_TRANSITION_PARAMS_LENGTH = 5;
    public static final String TAG = "GenericLevelSet";
    public final byte[] dstAddress;
    public final int mAszmic;
    public final Integer mDelay;
    public final int mLevel;
    public final Integer mTransitionResolution;
    public final Integer mTransitionSteps;

    public GenericLevelSet(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, boolean z, byte[] bArr, int i, Integer num, Integer num2, Integer num3, int i2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mAszmic = z ? 1 : 0;
        this.dstAddress = bArr;
        this.mMeshModel = meshModel;
        this.mAppKeyIndex = i;
        this.mTransitionSteps = num;
        this.mTransitionResolution = num2;
        this.mDelay = num3;
        this.mLevel = i2;
        createAccessMessage();
    }

    private void createAccessMessage() {
        ByteBuffer byteBufferOrder;
        a.a(TAG, "Level: " + this.mLevel);
        if (this.mTransitionSteps == null || this.mTransitionResolution == null || this.mDelay == null) {
            byteBufferOrder = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.putShort((short) this.mLevel);
            byteBufferOrder.put((byte) this.mProvisionedMeshNode.getSequenceNumber());
        } else {
            a.a(TAG, "Transition step " + this.mTransitionSteps + ",resolution: " + this.mTransitionResolution);
            byteBufferOrder = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.putShort((short) this.mLevel);
            byteBufferOrder.put((byte) this.mProvisionedMeshNode.getSequenceNumber());
            byteBufferOrder.put((byte) ((this.mTransitionResolution.intValue() << 6) | this.mTransitionSteps.intValue()));
            byteBufferOrder.put((byte) this.mDelay.intValue());
        }
        byte[] bArrArray = byteBufferOrder.array();
        byte[] byteArray = MeshParserUtils.toByteArray(this.mMeshModel.getBoundAppkeys().get(Integer.valueOf(this.mAppKeyIndex)));
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, 33286, bArrArray);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        a.a(TAG, "Sending Generic Level set acknowledged: " + this.mLevel);
        super.executeSend();
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.GENERIC_LEVEL_SET_STATE;
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

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
public class GenericOnOffSet extends GenericMessageState implements f {
    public static final int GENERIC_ON_OFF_SET_PARAMS_LENGTH = 2;
    public static final int GENERIC_ON_OFF_SET_TRANSITION_PARAMS_LENGTH = 4;
    public static final int GENERIC_ON_OFF_TRANSITION_STEP_0 = 0;
    public static final int GENERIC_ON_OFF_TRANSITION_STEP_1 = 1;
    public static final int GENERIC_ON_OFF_TRANSITION_STEP_2 = 2;
    public static final int GENERIC_ON_OFF_TRANSITION_STEP_3 = 3;
    public static final String TAG = "GenericOnOffSet";
    public final byte[] dstAddress;
    public final int mAszmic;
    public final Integer mDelay;
    public final boolean mState;
    public final Integer mTransitionResolution;
    public final Integer mTransitionSteps;

    public GenericOnOffSet(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, boolean z, byte[] bArr, int i, Integer num, Integer num2, Integer num3, boolean z2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mAszmic = z ? 1 : 0;
        this.dstAddress = bArr;
        this.mMeshModel = meshModel;
        this.mAppKeyIndex = i;
        this.mTransitionSteps = num;
        this.mTransitionResolution = num2;
        this.mDelay = num3;
        this.mState = z2;
        createAccessMessage();
    }

    private void createAccessMessage() {
        ByteBuffer byteBufferOrder;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("State: ");
        sb.append(this.mState ? "ON" : "OFF");
        a.a(str, sb.toString());
        if (this.mTransitionSteps == null || this.mTransitionResolution == null || this.mDelay == null) {
            byteBufferOrder = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(this.mState ? (byte) 1 : (byte) 0);
            byteBufferOrder.put((byte) this.mProvisionedMeshNode.getSequenceNumber());
        } else {
            a.a(TAG, "Transition step " + this.mTransitionSteps + ",resolution: " + this.mTransitionResolution);
            byteBufferOrder = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(this.mState ? (byte) 1 : (byte) 0);
            byteBufferOrder.put((byte) this.mProvisionedMeshNode.getSequenceNumber());
            byteBufferOrder.put((byte) ((this.mTransitionResolution.intValue() << 6) | this.mTransitionSteps.intValue()));
            byteBufferOrder.put((byte) this.mDelay.intValue());
        }
        byte[] bArrArray = byteBufferOrder.array();
        byte[] byteArray = MeshParserUtils.toByteArray(this.mMeshModel.getBoundAppkeys().get(Integer.valueOf(this.mAppKeyIndex)));
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, 33282, bArrArray);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Sending Generic OnOff set acknowledged: ");
        sb.append(this.mState ? "ON" : "OFF");
        a.a(str, sb.toString());
        super.executeSend();
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.GENERIC_ON_OFF_SET_STATE;
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

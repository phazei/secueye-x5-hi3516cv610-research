package meshprovisioner.configuration;

import android.content.Context;
import b.InterfaceC0369c;
import b.d.a;
import b.d.b;
import b.d.c;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public final class GenericOnOffStatus extends GenericMessageState {
    public static final int GENERIC_ON_OFF_STATE_ON = 1;
    public static final String TAG = "GenericOnOffStatus";
    public boolean mPresentOn;
    public int mRemainingTime;
    public Boolean mTargetOn;

    public GenericOnOffStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, int i) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mMeshModel = meshModel;
        this.mAppKeyIndex = i;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.GENERIC_ON_OFF_STATUS_STATE;
    }

    public final void parseGenericOnOffStatusMessage(a aVar) {
        int i;
        int i2;
        if (aVar == null) {
            throw new IllegalArgumentException("Access message cannot be null!");
        }
        a.a.a.a.b.m.a.a(TAG, "Received generic on off status");
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(aVar.o()).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.position(0);
        this.mPresentOn = byteBufferOrder.get() == 1;
        a.a.a.a.b.m.a.a(TAG, "Present on: " + this.mPresentOn);
        if (byteBufferOrder.limit() > 1) {
            this.mTargetOn = Boolean.valueOf(byteBufferOrder.get() == 1);
            this.mRemainingTime = byteBufferOrder.get() & 255;
            a.a.a.a.b.m.a.a(TAG, "Target on: " + this.mTargetOn);
            int i3 = this.mRemainingTime & 63;
            a.a.a.a.b.m.a.a(TAG, "Remaining time, transition number of steps: " + i3);
            int i4 = this.mRemainingTime >> 6;
            a.a.a.a.b.m.a.a(TAG, "Remaining time, transition number of step resolution: " + i4);
            a.a.a.a.b.m.a.a(TAG, "Remaining time: " + MeshParserUtils.getRemainingTime(this.mRemainingTime));
            i2 = i4;
            i = i3;
        } else {
            i = 0;
            i2 = 0;
        }
        this.mInternalTransportCallbacks.a(this.mProvisionedMeshNode);
        this.mMeshStatusCallbacks.onGenericOnOffStatusReceived(this.mProvisionedMeshNode, this.mPresentOn, this.mTargetOn, i, i2);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a.a.a.b.m.a.a(TAG, "Message reassembly may not be complete yet");
        } else if (pdu instanceof a) {
            a aVar = (a) pdu;
            if ((((aVar.u()[0] >> 7) & 1) + 1 == 2 ? (short) aVar.n() : (short) aVar.n()) == -32252) {
                parseGenericOnOffStatusMessage(aVar);
                return true;
            }
            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
        } else {
            parseControlMessage((b) pdu, this.mPayloads.size());
        }
        return false;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a.a.a.b.m.a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }

    public GenericOnOffStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }
}

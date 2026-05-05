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
public final class GenericLevelStatus extends GenericMessageState {
    public static final int GENERIC_LEVEL_STATUS_MANDATORY_LENGTH = 2;
    public static final String TAG = "GenericLevelStatus";

    public GenericLevelStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, MeshModel meshModel, int i) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mMeshModel = meshModel;
        this.mAppKeyIndex = i;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.GENERIC_LEVEL_STATUS_STATE;
    }

    public final void parseGenericLevelStatusMessage(a aVar) {
        short s;
        int i;
        int i2;
        if (aVar == null) {
            throw new IllegalArgumentException("Access message cannot be null!");
        }
        a.a.a.a.b.m.a.a(TAG, "Received generic level status");
        ByteBuffer byteBufferOrder = ByteBuffer.wrap(aVar.o()).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.position(0);
        short s2 = byteBufferOrder.getShort();
        a.a.a.a.b.m.a.a(TAG, "Present level: " + ((int) s2));
        if (byteBufferOrder.limit() > 2) {
            short s3 = byteBufferOrder.getShort();
            int i3 = byteBufferOrder.get() & 255;
            a.a.a.a.b.m.a.a(TAG, "Target level: " + ((int) s3));
            int i4 = i3 & 63;
            a.a.a.a.b.m.a.a(TAG, "Remaining time, transition number of steps: " + i4);
            int i5 = i3 >> 6;
            a.a.a.a.b.m.a.a(TAG, "Remaining time, transition number of step resolution: " + i5);
            a.a.a.a.b.m.a.a(TAG, "Remaining time: " + MeshParserUtils.getRemainingTime(i3));
            s = s3;
            i = i4;
            i2 = i5;
        } else {
            s = 0;
            i = 0;
            i2 = 0;
        }
        this.mInternalTransportCallbacks.a(this.mProvisionedMeshNode);
        this.mMeshStatusCallbacks.onGenericLevelStatusReceived(this.mProvisionedMeshNode, s2, s, i, i2);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a.a.a.b.m.a.a(TAG, "Message reassembly may not be complete yet");
        } else if (pdu instanceof a) {
            a aVar = (a) pdu;
            if ((((aVar.u()[0] >> 7) & 1) + 1 == 2 ? (short) aVar.n() : (short) aVar.n()) == -32248) {
                parseGenericLevelStatusMessage(aVar);
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

    public GenericLevelStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }
}

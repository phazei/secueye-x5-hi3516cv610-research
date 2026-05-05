package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import b.q;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public final class ConfigModelAppUnbind extends ConfigMessageState {
    public static final int SIG_MODEL_APP_KEY_BIND_PARAMS_LENGTH = 6;
    public static final String TAG = "ConfigModelAppUnbind";
    public static final int VENDOR_MODEL_APP_KEY_BIND_PARAMS_LENGTH = 8;
    public final int aid;
    public final int akf;
    public final int mAppKeyIndex;
    public final int mAszmic;
    public final byte[] mElementAddress;
    public final int mModelIdentifier;

    public ConfigModelAppUnbind(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, int i, byte[] bArr, int i2, int i3) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.akf = 0;
        this.aid = 0;
        this.mAszmic = i == 1 ? 1 : 0;
        this.mElementAddress = bArr;
        this.mModelIdentifier = i2;
        this.mAppKeyIndex = i3;
        createAccessMessage();
    }

    private void createAccessMessage() {
        byte[] bArrArray;
        byte[] bArrAddKeyIndexPadding = MeshParserUtils.addKeyIndexPadding(Integer.valueOf(this.mAppKeyIndex));
        int i = this.mModelIdentifier;
        if (i < -32768 || i > 32767) {
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(this.mElementAddress[1]);
            byteBufferOrder.put(this.mElementAddress[0]);
            byteBufferOrder.put(bArrAddKeyIndexPadding[1]);
            byteBufferOrder.put(bArrAddKeyIndexPadding[0]);
            int i2 = this.mModelIdentifier;
            byte[] bArr = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255)};
            byteBufferOrder.put(bArr[1]);
            byteBufferOrder.put(bArr[0]);
            byteBufferOrder.put(bArr[3]);
            byteBufferOrder.put(bArr[2]);
            bArrArray = byteBufferOrder.array();
        } else {
            ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder2.put(this.mElementAddress[1]);
            byteBufferOrder2.put(this.mElementAddress[0]);
            byteBufferOrder2.put(bArrAddKeyIndexPadding[1]);
            byteBufferOrder2.put(bArrAddKeyIndexPadding[0]);
            byteBufferOrder2.putShort((short) this.mModelIdentifier);
            bArrArray = byteBufferOrder2.array();
        }
        this.messageType = 32831;
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.mProvisionedMeshNode.getDeviceKey(), 0, 0, this.mAszmic, this.messageType, bArrArray);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        q qVar;
        a.a(TAG, "Sending config app unbind");
        super.executeSend();
        if (this.mPayloads.isEmpty() || (qVar = this.mMeshStatusCallbacks) == null) {
            return;
        }
        qVar.onAppKeyUnbindSent(this.mProvisionedMeshNode);
    }

    public byte[] getSrc() {
        return this.mSrc;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.CONFIG_MODEL_APP_UNBIND_STATE;
    }

    public void parseData(byte[] bArr) {
        parseMeshPdu(bArr);
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

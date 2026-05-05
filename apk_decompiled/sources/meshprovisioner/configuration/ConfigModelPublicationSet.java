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
import meshprovisioner.utils.ConfigModelPublicationSetParams;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigModelPublicationSet extends ConfigMessageState {
    public static final int SIG_MODEL_PUBLISH_SET_PARAMS_LENGTH = 11;
    public static final String TAG = "ConfigModelPublicationSet";
    public static final int VENDOR_MODEL_PUBLISH_SET_PARAMS_LENGTH = 13;
    public final int appKeyIndex;
    public final int aszmic;
    public final int credentialFlag;
    public final byte[] elementAddress;
    public final int mModelIdentifier;
    public int publicationResolution;
    public final int publicationSteps;
    public final byte[] publishAddress;
    public final int publishRetransmitCount;
    public final int publishRetransmitIntervalSteps;
    public final int publishTtl;

    public ConfigModelPublicationSet(Context context, ConfigModelPublicationSetParams configModelPublicationSetParams, InterfaceC0369c interfaceC0369c) {
        super(context, configModelPublicationSetParams.getMeshNode(), interfaceC0369c);
        this.aszmic = configModelPublicationSetParams.getAszmic();
        this.elementAddress = configModelPublicationSetParams.getElementAddress();
        this.publishAddress = configModelPublicationSetParams.getPublishAddress();
        this.mModelIdentifier = configModelPublicationSetParams.getModelIdentifier();
        this.appKeyIndex = configModelPublicationSetParams.getAppKeyIndex();
        this.credentialFlag = configModelPublicationSetParams.getCredentialFlag() ? 1 : 0;
        this.publishTtl = configModelPublicationSetParams.getPublishTtl();
        this.publicationSteps = configModelPublicationSetParams.getPublicationSteps();
        this.publicationResolution = configModelPublicationSetParams.getPublicationResolution();
        this.publishRetransmitCount = configModelPublicationSetParams.getPublishRetransmitCount();
        this.publishRetransmitIntervalSteps = configModelPublicationSetParams.getPublishRetransmitIntervalSteps();
        createAccessMessage();
    }

    private void createAccessMessage() {
        byte[] bArrArray;
        byte[] bArrAddKeyIndexPadding = MeshParserUtils.addKeyIndexPadding(Integer.valueOf(this.appKeyIndex));
        int i = (bArrAddKeyIndexPadding[0] << 4) | this.credentialFlag;
        int i2 = (this.publishRetransmitCount << 5) | (this.publishRetransmitIntervalSteps & 31);
        int i3 = this.mModelIdentifier;
        if (i3 < -32768 || i3 > 32767) {
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(13).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(this.elementAddress[1]);
            byteBufferOrder.put(this.elementAddress[0]);
            byteBufferOrder.put(this.publishAddress[1]);
            byteBufferOrder.put(this.publishAddress[0]);
            byteBufferOrder.put(bArrAddKeyIndexPadding[1]);
            byteBufferOrder.put((byte) i);
            byteBufferOrder.put((byte) this.publishTtl);
            byteBufferOrder.put((byte) (this.publicationSteps | this.publicationResolution));
            byteBufferOrder.put((byte) i2);
            int i4 = this.mModelIdentifier;
            byte[] bArr = {(byte) ((i4 >> 24) & 255), (byte) ((i4 >> 16) & 255), (byte) ((i4 >> 8) & 255), (byte) (i4 & 255)};
            byteBufferOrder.put(bArr[1]);
            byteBufferOrder.put(bArr[0]);
            byteBufferOrder.put(bArr[3]);
            byteBufferOrder.put(bArr[2]);
            bArrArray = byteBufferOrder.array();
        } else {
            ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(11).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder2.put(this.elementAddress[1]);
            byteBufferOrder2.put(this.elementAddress[0]);
            byteBufferOrder2.put(this.publishAddress[1]);
            byteBufferOrder2.put(this.publishAddress[0]);
            byteBufferOrder2.put(bArrAddKeyIndexPadding[1]);
            byteBufferOrder2.put((byte) i);
            byteBufferOrder2.put((byte) this.publishTtl);
            byteBufferOrder2.put((byte) (this.publicationSteps | this.publicationResolution));
            byteBufferOrder2.put((byte) i2);
            byteBufferOrder2.putShort((short) this.mModelIdentifier);
            bArrArray = byteBufferOrder2.array();
        }
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.mProvisionedMeshNode.getDeviceKey(), 0, 0, 0, 3, bArrArray);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        q qVar;
        a.a(TAG, "Sending config model publication set");
        super.executeSend();
        if (this.mPayloads.isEmpty() || (qVar = this.mMeshStatusCallbacks) == null) {
            return;
        }
        qVar.onPublicationSetSent(this.mProvisionedMeshNode);
    }

    public byte[] getSrc() {
        return this.mSrc;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.CONFIG_MODEL_PUBLICATION_SET_STATE;
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

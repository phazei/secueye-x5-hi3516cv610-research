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
public class ConfigAppKeyAdd extends ConfigMessageState {
    public final String TAG;
    public final String mAppKey;
    public final int mAppKeyIndex;
    public final int mAszmic;

    public ConfigAppKeyAdd(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, int i, String str, int i2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.TAG = ConfigAppKeyAdd.class.getSimpleName();
        this.mAszmic = i != 1 ? 0 : 1;
        this.mAppKey = str;
        this.mAppKeyIndex = i2;
        createAccessMessage();
    }

    private void createAccessMessage() {
        byte[] keyIndex = this.mProvisionedMeshNode.getKeyIndex();
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        byte[] bArrAddKeyIndexPadding = MeshParserUtils.addKeyIndexPadding(Integer.valueOf(this.mAppKeyIndex));
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(19).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(keyIndex[1]);
        byteBufferOrder.put((byte) ((keyIndex[0] & 15) | (bArrAddKeyIndexPadding[1] << 4)));
        byteBufferOrder.put((byte) ((bArrAddKeyIndexPadding[0] << 4) | (bArrAddKeyIndexPadding[1] >> 4)));
        byteBufferOrder.put(byteArray);
        byte[] bArrArray = byteBufferOrder.array();
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.mProvisionedMeshNode.getDeviceKey(), 0, 0, this.mAszmic, 0, bArrArray);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        q qVar;
        a.a(this.TAG, "Sending config app key add");
        super.executeSend();
        if (this.mPayloads.isEmpty() || (qVar = this.mMeshStatusCallbacks) == null) {
            return;
        }
        qVar.onAppKeyAddSent(this.mProvisionedMeshNode);
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public byte[] getSrc() {
        return this.mSrc;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.APP_KEY_ADD_STATE;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a(this.TAG, "Message reassembly may not be complete yet");
        } else {
            if (!(pdu instanceof b.d.a)) {
                parseControlMessage((b) pdu, this.mPayloads.size());
                return true;
            }
            byte[] bArrU = ((b.d.a) pdu).u();
            a.a(this.TAG, "Unexpected access message received: " + MeshParserUtils.bytesToHex(bArrU, false));
        }
        return false;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a(this.TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}

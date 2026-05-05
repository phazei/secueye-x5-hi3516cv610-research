package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import b.q;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigCompositionDataGet extends ConfigMessageState {
    public static final String TAG = "ConfigCompositionDataGet";
    public int aid;
    public int akf;
    public int mAszmic;

    public ConfigCompositionDataGet(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c, int i) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.akf = 0;
        this.aid = 0;
        this.mAszmic = i == 1 ? 1 : 0;
        createAccessMessage();
    }

    private void createAccessMessage() {
        MeshTransport meshTransport = this.mMeshTransport;
        ProvisionedMeshNode provisionedMeshNode = this.mProvisionedMeshNode;
        this.message = meshTransport.createMeshMessage(provisionedMeshNode, this.mSrc, provisionedMeshNode.getDeviceKey(), this.akf, this.aid, this.mAszmic, 32776, new byte[]{-1});
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final void executeSend() {
        q qVar;
        a.a(TAG, "Sending composition data get");
        super.executeSend();
        if (this.mPayloads.isEmpty() || (qVar = this.mMeshStatusCallbacks) == null) {
            return;
        }
        qVar.onGetCompositionDataSent(this.mProvisionedMeshNode);
    }

    public byte[] getSrc() {
        return this.mSrc;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.COMPOSITION_DATA_GET_STATE;
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
    }
}

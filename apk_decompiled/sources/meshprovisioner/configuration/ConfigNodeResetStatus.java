package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public final class ConfigNodeResetStatus extends ConfigMessageState {
    public static final String TAG = "ConfigNodeResetStatus";

    public ConfigNodeResetStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.CONFIG_NODE_RESET_STATUS_STATE;
    }

    public void parseData(byte[] bArr) {
        parseMeshPdu(bArr);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a(TAG, "Message reassembly may not be complete yet");
        } else if (pdu instanceof b.d.a) {
            b.d.a aVar = (b.d.a) pdu;
            if ((((aVar.u()[0] >> 7) & 1) + 1 == 2 ? (short) aVar.n() : (short) aVar.n()) == -32694) {
                a.a(TAG, "Received node reset status");
                this.mInternalTransportCallbacks.b(this.mProvisionedMeshNode);
                this.mMeshStatusCallbacks.onMeshNodeResetStatusReceived(this.mProvisionedMeshNode);
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
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}

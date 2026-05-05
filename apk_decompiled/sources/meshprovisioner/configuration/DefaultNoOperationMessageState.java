package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public class DefaultNoOperationMessageState extends MeshMessageState {
    public static final String TAG = "DefaultNoOperationMessageState";

    public DefaultNoOperationMessageState(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return null;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a(TAG, "Message reassembly may not be completed yet!");
        } else {
            if (pdu instanceof b.d.a) {
                b.d.a aVar = (b.d.a) pdu;
                byte[] bArrU = aVar.u();
                int i = (bArrU[0] & 240) >> 6;
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            a.a(TAG, "Unknown Access PDU Received: " + MeshParserUtils.bytesToHex(bArrU, false));
                            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
                        } else {
                            a.a(TAG, "Vendor model Access PDU Received: " + MeshParserUtils.bytesToHex(bArrU, false));
                            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
                        }
                    } else if (pdu.n() == -32252) {
                        GenericOnOffStatus genericOnOffStatus = new GenericOnOffStatus(this.mContext, this.mProvisionedMeshNode, this.meshMessageHandlerCallbacks);
                        genericOnOffStatus.setTransportCallbacks(this.mInternalTransportCallbacks);
                        genericOnOffStatus.setStatusCallbacks(this.mMeshStatusCallbacks);
                        genericOnOffStatus.parseGenericOnOffStatusMessage(aVar);
                    } else if (pdu.n() == -32248) {
                        GenericLevelStatus genericLevelStatus = new GenericLevelStatus(this.mContext, this.mProvisionedMeshNode, this.meshMessageHandlerCallbacks);
                        genericLevelStatus.setTransportCallbacks(this.mInternalTransportCallbacks);
                        genericLevelStatus.setStatusCallbacks(this.mMeshStatusCallbacks);
                        genericLevelStatus.parseGenericLevelStatusMessage(aVar);
                    } else {
                        a.a(TAG, "Unknown Access PDU Received: " + MeshParserUtils.bytesToHex(bArrU, false));
                    }
                }
                return true;
            }
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

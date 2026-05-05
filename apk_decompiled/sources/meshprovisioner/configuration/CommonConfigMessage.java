package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.e.i;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class CommonConfigMessage extends CommonMessageState {
    public static final String TAG = "CommonConfigMessage";
    public final byte[] dstAddress;
    public final int mAszmic;
    public byte[] mDeviceKey;
    public final int opCode;
    public final byte[] parameters;
    public MeshMessageState.MessageState state;

    public CommonConfigMessage(Context context, ProvisionedMeshNode provisionedMeshNode, boolean z, InterfaceC0369c interfaceC0369c, boolean z2, byte[] bArr, int i, byte[] bArr2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mDeviceKey = provisionedMeshNode.getDeviceKey();
        this.mAszmic = z2 ? 1 : 0;
        this.dstAddress = bArr;
        this.opCode = i;
        this.parameters = bArr2;
        MeshMessageState.MessageState[] messageStateArrValues = MeshMessageState.MessageState.values();
        int length = messageStateArrValues.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            MeshMessageState.MessageState messageState = messageStateArrValues[i2];
            if (messageState.getState() == this.opCode) {
                this.state = messageState;
                break;
            }
            i2++;
        }
        if (z) {
            createAccessMessage();
        }
    }

    private void createAccessMessage() {
        this.message = i.c().a(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, this.mDeviceKey, 0, SecureUtils.calculateK4(this.mDeviceKey), this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return this.state;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}

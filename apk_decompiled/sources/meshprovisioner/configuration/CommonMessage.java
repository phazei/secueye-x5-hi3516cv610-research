package meshprovisioner.configuration;

import a.a.a.a.b.G;
import a.a.a.a.b.m.a;
import android.content.Context;
import androidx.core.view.MotionEventCompat;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.util.Arrays;
import java.util.Set;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class CommonMessage extends CommonMessageState {
    public static final String TAG = "CommonMessage";
    public final byte[] dstAddress;
    public String mAppKey;
    public final int mAszmic;
    public final int opCode;
    public final byte[] parameters;
    public MeshMessageState.MessageState state;

    public CommonMessage(Context context, ProvisionedMeshNode provisionedMeshNode, boolean z, InterfaceC0369c interfaceC0369c, String str, boolean z2, byte[] bArr, int i, int i2, byte[] bArr2) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mAppKey = str;
        this.mAszmic = z2 ? 1 : 0;
        this.dstAddress = bArr;
        this.mAppKeyIndex = i;
        this.opCode = i2;
        this.parameters = bArr2;
        MeshMessageState.MessageState[] messageStateArrValues = MeshMessageState.MessageState.values();
        int length = messageStateArrValues.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            MeshMessageState.MessageState messageState = messageStateArrValues[i3];
            if (messageState.getState() == this.opCode) {
                this.state = messageState;
                break;
            }
            i3++;
        }
        if (z) {
            createAccessMessage();
        } else {
            createProxyConfigMessage();
        }
    }

    private void createAccessMessage() {
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        this.message = this.mMeshTransport.createMeshMessage(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    private void createProxyConfigMessage() {
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        this.message = this.mMeshTransport.createProxyConfigMessage(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public void executeSend() {
        a.a(TAG, "Sending common message");
        super.executeSend();
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return this.state;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public boolean parseMeshPdu(byte[] bArr) {
        try {
            c pdu = this.mMeshTransport.parsePdu(bArr);
            if (pdu != null) {
                byte[] bArrR = pdu.r();
                byte[] bArrF = pdu.f();
                a.d(TAG, "Received an message, src(" + Utils.bytes2HexString(bArrR) + "), dst(" + Utils.bytes2HexString(bArrF) + ")");
                ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) G.a().b();
                Set<Integer> flatSubscribeGroupAddress = provisionedMeshNode.getFlatSubscribeGroupAddress();
                String str = "[";
                for (Integer num : flatSubscribeGroupAddress) {
                    str = (str + Utils.bytes2HexString(new byte[]{(byte) ((num.intValue() >> 8) & 255), (byte) (num.intValue() & 255)})) + ",";
                }
                String str2 = TAG;
                a.a(str2, "Self subscribe address: " + (str + "]"));
                a.a(TAG, "Self unicast address: " + Utils.bytes2HexString(provisionedMeshNode.getUnicastAddress()));
                int i = ((bArrF[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (bArrF[1] & 255);
                if (!Arrays.equals(provisionedMeshNode.getUnicastAddress(), bArrF) && !flatSubscribeGroupAddress.contains(Integer.valueOf(i))) {
                    a.d(TAG, String.format("Received an access message that was not directed to us(%s), let's drop it", Utils.bytes2HexString(bArrF)));
                    return false;
                }
                if (!(pdu instanceof b.d.a)) {
                    parseControlMessage((b) pdu, this.mPayloads.size());
                    return true;
                }
                b.d.a aVar = (b.d.a) pdu;
                int i2 = (aVar.u()[0] & 240) >> 6;
                if (i2 == 0) {
                    i2 = 1;
                }
                this.mMeshStatusCallbacks.onCommonMessageStatusReceived(this.mProvisionedMeshNode, pdu.r(), Integer.toHexString(i2 == 1 ? aVar.n() & 255 : i2 == 2 ? aVar.n() & 65535 : aVar.n() & 16777215), aVar.o(), null);
            } else {
                a.a(TAG, "Message reassembly may not be complete yet");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}

package meshprovisioner.configuration;

import a.a.a.a.b.G;
import a.a.a.a.b.m.a;
import android.content.Context;
import android.text.TextUtils;
import androidx.core.view.MotionEventCompat;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import b.e.i;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.StringUtils;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import meshprovisioner.ProxyProtocolMessageType;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: loaded from: classes4.dex */
public class CommonMessageV2 extends CommonMessageState {
    public static final String TAG = "CommonMessageV2";
    public final byte[] dstAddress;
    public boolean mAkf;
    public String mAppKey;
    public final int mAszmic;
    public final int opCode;
    public final byte[] parameters;
    public MeshMessageState.MessageState state;

    /* JADX INFO: renamed from: meshprovisioner.configuration.CommonMessageV2$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$ProxyProtocolMessageType = new int[ProxyProtocolMessageType.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$ProxyProtocolMessageType[ProxyProtocolMessageType.NetworkPDU.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$ProxyProtocolMessageType[ProxyProtocolMessageType.ProxyConfiguration.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CommonMessageV2(Context context, ProvisionedMeshNode provisionedMeshNode, ProxyProtocolMessageType proxyProtocolMessageType, boolean z, InterfaceC0369c interfaceC0369c, String str, boolean z2, byte[] bArr, int i, int i2, byte[] bArr2) {
        Object[] objArr;
        super(context, provisionedMeshNode, interfaceC0369c);
        this.mAkf = true;
        this.mAppKey = str;
        this.mAszmic = z2 ? 1 : 0;
        this.dstAddress = bArr;
        this.mAppKeyIndex = i;
        this.opCode = i2;
        this.parameters = bArr2;
        Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
        int i3 = 0;
        if (addedAppKeys != null) {
            objArr = false;
            for (String str2 : addedAppKeys.values()) {
                if (!TextUtils.isEmpty(str) && StringUtils.equalsIgnoreCase(str, str2)) {
                    objArr = true;
                }
            }
        } else {
            objArr = true;
        }
        if (objArr == false) {
            this.mAkf = false;
        }
        if (AddressUtils.isValidGroupAddress(bArr)) {
            this.mAkf = true;
        }
        MeshMessageState.MessageState[] messageStateArrValues = MeshMessageState.MessageState.values();
        int length = messageStateArrValues.length;
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
        int i4 = AnonymousClass1.$SwitchMap$meshprovisioner$ProxyProtocolMessageType[proxyProtocolMessageType.ordinal()];
        if (i4 != 1) {
            if (i4 != 2) {
                return;
            }
            createProxyConfigMessage();
        } else if (z) {
            createAccessMessage();
        } else {
            createControlMessage();
        }
    }

    private void createAccessMessage() {
        if (TextUtils.isEmpty(this.mAppKey)) {
            return;
        }
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        boolean z = this.mAkf;
        byte bCalculateK4 = SecureUtils.calculateK4(byteArray);
        this.message = i.c().a(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, z ? 1 : 0, bCalculateK4, this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    private void createControlMessage() {
        if (TextUtils.isEmpty(this.mAppKey)) {
            return;
        }
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        this.message = i.c().a(this.mProvisionedMeshNode, 0, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, this.opCode, this.parameters);
        this.mPayloads.putAll(this.message.m());
    }

    private void createProxyConfigMessage() {
        if (TextUtils.isEmpty(this.mAppKey)) {
            return;
        }
        byte[] byteArray = MeshParserUtils.toByteArray(this.mAppKey);
        this.message = i.c().b(this.mProvisionedMeshNode, this.mSrc, this.dstAddress, byteArray, 1, SecureUtils.calculateK4(byteArray), this.mAszmic, this.opCode, this.parameters);
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

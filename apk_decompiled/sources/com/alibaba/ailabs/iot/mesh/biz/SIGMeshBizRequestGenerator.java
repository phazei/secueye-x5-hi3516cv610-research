package com.alibaba.ailabs.iot.mesh.biz;

import a.a.a.a.b.a.A;
import a.a.a.a.b.a.B;
import a.a.a.a.b.a.C;
import a.a.a.a.b.a.C0324j;
import a.a.a.a.b.a.C0325k;
import a.a.a.a.b.a.D;
import a.a.a.a.b.a.E;
import a.a.a.a.b.a.F;
import a.a.a.a.b.a.G;
import a.a.a.a.b.a.H;
import a.a.a.a.b.a.I;
import a.a.a.a.b.a.l;
import a.a.a.a.b.a.m;
import a.a.a.a.b.a.n;
import a.a.a.a.b.a.o;
import a.a.a.a.b.a.p;
import a.a.a.a.b.a.q;
import a.a.a.a.b.a.r;
import a.a.a.a.b.a.s;
import a.a.a.a.b.a.t;
import a.a.a.a.b.a.v;
import a.a.a.a.b.a.w;
import a.a.a.a.b.a.x;
import a.a.a.a.b.a.y;
import a.a.a.a.b.a.z;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import b.u;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.xiaomi.mipush.sdk.Constants;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class SIGMeshBizRequestGenerator {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static byte f2810a = 1;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static a f2811b = new r();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static a f2812c = new y();

    public enum Attribute {
        powerstate("powerstate", new byte[]{0, 1}, (byte) 1, new C()),
        brightness("brightness", new byte[]{33, 1}, (byte) 2, new D()),
        colorTemperature("colorTemperature", new byte[]{34, 1}, (byte) 2, new E()),
        curtainConrtol("curtainConrtol", new byte[]{71, 5}, (byte) 1, new F()),
        curtainPosition("curtainPosition", new byte[]{72, 5}, (byte) 1, new G()),
        mode("mode", new byte[]{4, -16}, (byte) 2, new H()),
        sceneA("sceneA", new byte[]{-120, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneB("sceneB", new byte[]{-119, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneC("sceneC", new byte[]{-118, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneD("sceneD", new byte[]{-117, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneE("sceneE", new byte[]{-116, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneF("sceneF", new byte[]{-115, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneG("sceneG", new byte[]{-114, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneH("sceneH", new byte[]{-113, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneI("sceneI", new byte[]{-112, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneJ("sceneJ", new byte[]{-111, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneK("sceneK", new byte[]{-110, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneL("sceneL", new byte[]{-109, 3}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneOn("sceneOn", new byte[]{96, 4}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        sceneOff("sceneOff", new byte[]{97, 4}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        localScene("localScene", new byte[]{98, 4}, (byte) 2, SIGMeshBizRequestGenerator.f2811b),
        funcSwitch("funcSwitch", new byte[]{99, 4}, (byte) 1, SIGMeshBizRequestGenerator.f2812c),
        relayDisable("relayDisable", new byte[]{100, 4}, (byte) 1, SIGMeshBizRequestGenerator.f2812c);

        public byte attrParameterLength;
        public byte[] attrType;
        public String attributeName;
        public a valueEncoder;

        Attribute(String str, byte[] bArr, byte b2, a aVar) {
            this.attributeName = str;
            this.attrType = bArr;
            this.attrParameterLength = b2;
            this.valueEncoder = aVar;
        }

        public a getValueEncoder() {
            return this.valueEncoder;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public interface a<T> {
        byte[] a(T t);
    }

    public static byte d() {
        byte b2 = f2810a;
        if (b2 == 0) {
            b2 = (byte) (b2 + 1);
            f2810a = b2;
        }
        f2810a = b2;
        byte b3 = f2810a;
        f2810a = (byte) (b3 + 1);
        return (byte) (b3 % 255);
    }

    public static SIGMeshBizRequest e() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.putInt((int) (jCurrentTimeMillis / 1000));
        byte[] bArr = {0, 31, -16, 0, 0, 0, 0, (byte) ((TimeZone.getDefault().getRawOffset() / 3600) / 1000)};
        System.arraycopy(byteBufferOrder.array(), 0, bArr, 3, 4);
        return a(AddressUtils.getUnicastAddressInt(new byte[]{-49, -1}), "D2A801", MeshParserUtils.bytesToHex(bArr, false), (IActionListener<Boolean>) null);
    }

    public static SIGMeshBizRequest a(Context context, String str, short s, Map<String, Object> map, IActionListener<Boolean> iActionListener) {
        String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
        ProvisionedMeshNode provisionedMeshNodeB = a.a.a.a.b.G.a().d().b(strCoverIotIdToDevId);
        if (provisionedMeshNodeB != null) {
            return new SIGMeshBizRequest.a().a(5).a(SIGMeshBizRequest.Type.SCENE_SETUP_STORE).a(provisionedMeshNodeB).a(provisionedMeshNodeB.getUnicastAddress()).a(Utils.deviceId2Mac(strCoverIotIdToDevId)).a(a(context)).a(new z(map != null && (map.containsKey(Attribute.curtainConrtol.attributeName) || map.containsKey(Attribute.curtainPosition.attributeName) || map.containsKey(Attribute.mode.attributeName)), s, map)).a(iActionListener).a();
        }
        Utils.notifyFailed(iActionListener, -53, "meshNode is null");
        return null;
    }

    public static SIGMeshBizRequest b(Context context, String str, Map<String, Short> map, IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "getSceneUnBindRequest() called with: context = [" + context + "], devId = [" + str + "], attributeSceneIdMap = [" + map + "], callback = [" + iActionListener + "]");
        ProvisionedMeshNode provisionedMeshNodeB = a.a.a.a.b.G.a().d().b(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str));
        if (map == null || map.isEmpty()) {
            a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "Illegal attribute map.");
            Utils.notifyFailed(iActionListener, -53, "Illegal attribute map.");
            return null;
        }
        int length = 3;
        for (Map.Entry<String, Short> entry : map.entrySet()) {
            if (entry == null || TextUtils.isEmpty(entry.getKey()) || entry.getValue() == null) {
                a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "Illegal attribute map item.");
                Utils.notifyFailed(iActionListener, -53, "Illegal attribute map item.");
                return null;
            }
            try {
                Attribute attributeValueOf = Attribute.valueOf(entry.getKey());
                length = length + attributeValueOf.attrType.length + attributeValueOf.attrParameterLength;
                StringBuilder sb = new StringBuilder();
                sb.append("getSceneBindRequest() called with: length=");
                sb.append(attributeValueOf.attrType.length);
                sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                sb.append((int) attributeValueOf.attrParameterLength);
                a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", sb.toString());
            } catch (Exception e) {
                a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", e.toString());
                Utils.notifyFailed(iActionListener, -53, "Illegal attribute");
                return null;
            }
        }
        byte bD = d();
        if (provisionedMeshNodeB == null) {
            a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "meshNode == null");
            Utils.notifyFailed(iActionListener, -53, "meshNode is null");
            return null;
        }
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "getSceneBindRequest() called with: needCapacity=" + length);
        return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.VENDOR_ATTRIBUTE_SET).a(provisionedMeshNodeB).a(ConvertUtils.hexString2Bytes("CFFF")).a(true).a(new n(iActionListener)).a(new p(length, map)).a(new o(a(map, length, bD))).a();
    }

    public static SIGMeshBizRequest c(Context context, String str, Map<String, Short> map, IActionListener<Boolean> iActionListener) {
        if (map != null && !map.isEmpty()) {
            return b(context, str, map, iActionListener);
        }
        a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "getSceneUnbindRequest Illegal attribute.");
        Utils.notifyFailed(iActionListener, -53, "Illegal attribute.");
        return null;
    }

    public static SIGMeshBizRequest a(Context context, String str, short s, IActionListener<Boolean> iActionListener) {
        ProvisionedMeshNode provisionedMeshNodeB = a.a.a.a.b.G.a().d().b(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str));
        if (provisionedMeshNodeB == null) {
            Utils.notifyFailed(iActionListener, -53, "meshNode is null");
            return null;
        }
        return new SIGMeshBizRequest.a().a(5).a(SIGMeshBizRequest.Type.SCENE_SETUP_DELETE).a(provisionedMeshNodeB).a(provisionedMeshNodeB.getUnicastAddress()).a(iActionListener).a(a(context)).a(new A(s)).a();
    }

    public static SIGMeshBizRequest a(String str, short s, IActionListener<Boolean> iActionListener) {
        String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
        u uVarD = a.a.a.a.b.G.a().d();
        ProvisionedMeshNode provisionedMeshNodeB = uVarD.b(strCoverIotIdToDevId);
        u.a aVarC = uVarD.c(strCoverIotIdToDevId);
        if (provisionedMeshNodeB == null || aVarC == null) {
            return null;
        }
        if (aVarC.i()) {
            return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.SCENE_RECALL_UNACKNOWLEDGED).a(provisionedMeshNodeB).a(new byte[]{-49, -1}).a(false).a(iActionListener).a(new B(s)).a();
        }
        return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.VENDOR_DELEGATE_PROTOCOL).a(provisionedMeshNodeB).a(new byte[]{-49, -1}).a(false).a(iActionListener).a(new C0324j(s)).a();
    }

    public static SIGMeshBizRequest a(byte[] bArr, Map<String, Object> map) {
        u.a aVarD;
        ProvisionedMeshNode provisionedMeshNode = (!AddressUtils.isValidUnicastAddress(bArr) || (aVarD = a.a.a.a.b.G.a().d().d()) == null) ? null : (ProvisionedMeshNode) aVarD.d(bArr);
        if (provisionedMeshNode == null) {
            provisionedMeshNode = (ProvisionedMeshNode) a.a.a.a.b.G.a().b();
        }
        return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.VENDOR_ATTRIBUTE_SET_UNACKNOWLEDGED).a(provisionedMeshNode).a(bArr).a(false).a(new C0325k(map)).a();
    }

    public static SIGMeshBizRequest a(Context context, String str, String str2, short s, IActionListener<Boolean> iActionListener) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            HashMap map = new HashMap();
            map.put(str2, Short.valueOf(s));
            return a(context, str, map, iActionListener);
        }
        a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "getSceneBindRequest Illegal attribute.");
        Utils.notifyFailed(iActionListener, -53, "Illegal attribute.");
        return null;
    }

    public static SIGMeshBizRequest a(Context context, String str, Map<String, Short> map, IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "getSceneBindRequest() called with: context = [" + context + "], devId = [" + str + "], attributeSceneIdMap = [" + map + "], callback = [" + iActionListener + "]");
        ProvisionedMeshNode provisionedMeshNodeB = a.a.a.a.b.G.a().d().b(MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str));
        if (map != null && !map.isEmpty()) {
            int length = 1;
            for (Map.Entry<String, Short> entry : map.entrySet()) {
                if (entry != null && !TextUtils.isEmpty(entry.getKey()) && entry.getValue() != null) {
                    try {
                        Attribute attributeValueOf = Attribute.valueOf(entry.getKey());
                        length = length + attributeValueOf.attrType.length + attributeValueOf.attrParameterLength;
                    } catch (Exception e) {
                        a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", e.toString());
                        Utils.notifyFailed(iActionListener, -53, "Illegal attribute");
                        return null;
                    }
                } else {
                    a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "Illegal attribute map item.");
                    Utils.notifyFailed(iActionListener, -53, "Illegal attribute map item.");
                    return null;
                }
            }
            byte bD = d();
            if (provisionedMeshNodeB == null) {
                a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "meshNode == null");
                Utils.notifyFailed(iActionListener, -53, "meshNode is null");
                return null;
            }
            return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.VENDOR_ATTRIBUTE_SET).a(provisionedMeshNodeB).a(provisionedMeshNodeB.getUnicastAddress()).a(true).a(iActionListener).a(new m(length, map)).a(new l(length, bD, map)).a();
        }
        a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", "Illegal attribute map.");
        Utils.notifyFailed(iActionListener, -53, "Illegal attribute map.");
        return null;
    }

    @NonNull
    public static ByteBuffer a(Map<String, Short> map, int i, byte b2) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.put(b2);
        byteBufferOrder.put(new byte[]{Constants.CMD_TYPE.CMD_DEVICE_ABILITY_RES, -16});
        for (Map.Entry<String, Short> entry : map.entrySet()) {
            byteBufferOrder.put(Attribute.valueOf(entry.getKey()).attrType);
            byteBufferOrder.put(Attribute.valueOf(entry.getKey()).getValueEncoder().a(entry.getValue()));
        }
        return byteBufferOrder;
    }

    public static SIGMeshBizRequest a(Context context, String str, String str2, IActionListener<Boolean> iActionListener) {
        return a(context, str, str2, (short) 0, iActionListener);
    }

    public static SIGMeshBizRequest a(Context context, String str, String str2, int i, int i2, IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "removeNodeAfterUnbind: getDeviceResetRequest");
        ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) a.a.a.a.b.G.a().d().d().a(i2);
        provisionedMeshNode.setDeviceKey(MeshParserUtils.toByteArray(str2));
        try {
            a.a.a.a.b.e.a.a(provisionedMeshNode.getDevId());
        } catch (Exception unused) {
        }
        SIGMeshBizRequest sIGMeshBizRequestA = new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.COMMON_DEVICE_REST_NODE).a(provisionedMeshNode).a(provisionedMeshNode.getUnicastAddress()).a(true).a(iActionListener).a();
        sIGMeshBizRequestA.a(MeshParserUtils.toByteArray(str2));
        return sIGMeshBizRequestA;
    }

    public static SIGMeshBizRequest a(byte[] bArr, int i, IActionListener<Boolean> iActionListener) {
        ProvisionedMeshNode provisionedMeshNode;
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "updateMeshParameterRequest: getDeviceResetRequest");
        try {
            provisionedMeshNode = (ProvisionedMeshNode) ((ProvisionedMeshNode) a.a.a.a.b.G.a().d().d().b()).clone();
            try {
                provisionedMeshNode.setTtl(i);
            } catch (CloneNotSupportedException e) {
                e = e;
                e.printStackTrace();
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
            provisionedMeshNode = null;
        }
        return new SIGMeshBizRequest.a().a(SIGMeshBizRequest.Type.UPDATE_MESH_PARAMETER).a(provisionedMeshNode).a(new q(bArr)).a(ConvertUtils.hexString2Bytes("CFFF")).a(true).a(iActionListener).a();
    }

    public static SIGMeshBizRequest a(Context context, String str, String str2, int i, int i2, int i3, int i4, IActionListener<Boolean> iActionListener) {
        SIGMeshBizRequest.Type type = SIGMeshBizRequest.Type.COMMON_REQUEST_RESPONSE;
        type.setOpcode(Integer.parseInt(TmpConstant.GROUP_OP_ADD.equals(str) ? "801B" : "801C", 16));
        type.setExpectedOpcode((short) Integer.parseInt("801F", 16));
        u.a aVarD = a.a.a.a.b.G.a().d().d();
        ProvisionedMeshNode provisionedMeshNode = aVarD != null ? (ProvisionedMeshNode) aVarD.d(AddressUtils.getUnicastAddressBytes(i)) : null;
        if (provisionedMeshNode != null) {
            provisionedMeshNode.setDeviceKey(MeshParserUtils.toByteArray(str2));
        }
        SIGMeshBizRequest sIGMeshBizRequestA = new SIGMeshBizRequest.a().a(5).a(type).a(provisionedMeshNode).a(AddressUtils.getUnicastAddressBytes(i)).a(new a.a.a.a.b.a.u(context)).a(true).a(new t(iActionListener)).a(new s(i2, i3, i4)).a();
        sIGMeshBizRequestA.a(ConvertUtils.hexString2Bytes(str2));
        return sIGMeshBizRequestA;
    }

    public static SIGMeshBizRequest a(int i, String str, String str2, IActionListener<Boolean> iActionListener) {
        ProvisionedMeshNode provisionedMeshNode;
        SIGMeshBizRequest.Type type = SIGMeshBizRequest.Type.COMMON_FIRE_AND_FORGET;
        type.setOpcode(Integer.parseInt(str, 16));
        byte[] unicastAddressBytes = AddressUtils.getUnicastAddressBytes(i);
        u.a aVarD = a.a.a.a.b.G.a().d().d();
        if (aVarD == null) {
            provisionedMeshNode = null;
        } else if (AddressUtils.isValidGroupAddress(unicastAddressBytes)) {
            provisionedMeshNode = (ProvisionedMeshNode) aVarD.b();
        } else {
            provisionedMeshNode = (ProvisionedMeshNode) aVarD.a(i);
        }
        return new SIGMeshBizRequest.a().a(type).a(provisionedMeshNode).a(AddressUtils.getUnicastAddressBytes(i)).a(false).a(new w(iActionListener)).a(new v(str2)).a();
    }

    public static byte[] a(byte b2, List<String> list, byte b3, int i) {
        byte[] bArr;
        byte b4;
        byte b5 = (byte) (a.a.a.a.b.d.a.f1316b ? 15 : 14);
        if (b2 == 8) {
            bArr = new byte[list.size() + 6];
        } else {
            bArr = new byte[list.size() + 10];
        }
        byte b6 = (byte) 1;
        int i2 = 0;
        bArr[0] = b5;
        byte b7 = (byte) (b6 + 1);
        bArr[b6] = b2;
        byte b8 = (byte) (b7 + 1);
        bArr[b7] = (byte) (bArr.length - 3);
        if (b2 == 8) {
            b4 = (byte) (b8 + 1);
            bArr[b8] = b3;
        } else {
            byte b9 = (byte) (b8 + 3);
            byte[] bArrArray = ByteBuffer.allocate(4).putInt(i).order(ByteOrder.LITTLE_ENDIAN).array();
            int length = bArrArray.length;
            while (i2 < length) {
                bArr[b9] = bArrArray[i2];
                i2++;
                b9 = (byte) (b9 - 1);
            }
            byte b10 = (byte) (b9 + 5);
            bArr[b10] = b3;
            b4 = (byte) (b10 + 1);
        }
        byte b11 = (byte) (b4 + 1);
        bArr[b4] = 100;
        byte b12 = (byte) (b11 + 1);
        bArr[b11] = 10;
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(it.next().replaceAll(":", ""));
            bArr[b12] = bArrHexString2Bytes[bArrHexString2Bytes.length - 1];
            b12 = (byte) (b12 + 1);
        }
        return bArr;
    }

    public static I<Object> a(Context context) {
        return new x(context);
    }
}

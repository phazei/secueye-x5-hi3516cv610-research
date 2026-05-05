package b;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.mesh.R;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.ProxyProtocolMessageType;
import meshprovisioner.configuration.MeshModel;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.ConfigModelPublicationSetParams;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: renamed from: b.l, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class C0378l implements InterfaceC0370d, InterfaceC0368b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2190a = "l";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final Map<Integer, ProvisionedMeshNode> f2191b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final s f2192c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f2193d;
    public byte[] e;
    public InterfaceC0379m f;
    public o g;
    public b.b.a h;
    public byte[] i;
    public int j;
    public byte[] k;
    public int l;
    public boolean m;
    public Handler n;
    public Runnable o;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: b.l$a */
    /* JADX INFO: compiled from: MeshManagerApi.java */
    class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public IActionListener<Object> f2194a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public Runnable f2195b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public IActionListener f2196c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public a.a.a.a.b.a.I f2197d;
        public byte[] e;
        public int f;
        public int g;
        public byte[] h;

        public a(byte[] bArr, int i, byte[] bArr2, int i2, a.a.a.a.b.a.I i3, IActionListener iActionListener) {
            this.h = null;
            this.e = bArr;
            this.f = i;
            this.f2197d = i3;
            this.f2196c = iActionListener;
            this.g = i2;
            if ((i == 13871105 || i == 13740033) && bArr2 != null && bArr2.length >= 3) {
                this.h = new byte[]{bArr2[1], bArr2[2]};
            }
            if (i2 > 0) {
                this.f2195b = new RunnableC0376j(this, C0378l.this);
            }
        }

        public void a() {
            if (this.f2195b != null) {
                C0378l.this.n.postDelayed(this.f2195b, this.g);
            }
            b.b.a aVar = C0378l.this.h;
            byte[] bArr = this.e;
            int i = this.f;
            byte[] bArr2 = this.h;
            C0377k c0377k = new C0377k(this);
            this.f2194a = c0377k;
            aVar.a(bArr, i, bArr2, c0377k);
        }
    }

    public C0378l(Context context) {
        this(context, null, null);
    }

    public final void e() {
        if (!this.m) {
            this.f2191b.clear();
            return;
        }
        SharedPreferences sharedPreferences = this.f2193d.getSharedPreferences(Utils.PROVISIONED_NODES_FILE, 0);
        Map<String, ?> all = sharedPreferences.getAll();
        if (all.isEmpty()) {
            return;
        }
        List<Integer> listA = a(all);
        this.f2191b.clear();
        Iterator<Integer> it = listA.iterator();
        while (it.hasNext()) {
            String string = sharedPreferences.getString(String.format(Locale.US, "0x%04X", Integer.valueOf(it.next().intValue())), null);
            if (string != null) {
                ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) JSON.parseObject(string, ProvisionedMeshNode.class);
                this.f2191b.put(Integer.valueOf(AddressUtils.getUnicastAddressInt(provisionedMeshNode.getUnicastAddress())), provisionedMeshNode);
            }
        }
    }

    public final void f() {
        int i;
        if (this.m && (i = this.f2193d.getSharedPreferences("CONFIGURATION_SRC", 0).getInt("SRC", 0)) != 0) {
            this.e = new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
        }
    }

    public boolean g(byte[] bArr) {
        return bArr != null && bArr[0] == 0;
    }

    public boolean h(byte[] bArr) {
        int i = ((bArr[0] & 255) << 8) | (bArr[1] & 255);
        if (!MeshParserUtils.validateUnicastAddressInput(this.f2193d, Integer.valueOf(i))) {
            return false;
        }
        if (this.f2191b.containsKey(Integer.valueOf(i))) {
            throw new IllegalArgumentException("Address already occupied by a node");
        }
        this.e = bArr;
        i();
        Iterator<Map.Entry<Integer, ProvisionedMeshNode>> it = this.f2191b.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getValue().setConfigurationSrc(this.e);
        }
        h();
        return true;
    }

    public final void i() {
        if (this.m) {
            SharedPreferences.Editor editorEdit = this.f2193d.getSharedPreferences("CONFIGURATION_SRC", 0).edit();
            byte[] bArr = this.e;
            editorEdit.putInt("SRC", (bArr[1] & 255) | ((bArr[0] & 255) << 8));
            editorEdit.apply();
        }
    }

    @Override // b.InterfaceC0370d
    public void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr) {
        if (baseMeshNode == null) {
            a.a.a.a.b.m.a.b(f2190a, "sendPdu, empty mesh node");
            return;
        }
        int mtu = this.f.getMtu();
        if (baseMeshNode.supportFastProvision && !baseMeshNode.supportFastGattProvision) {
            this.f.sendPdu(baseMeshNode, bArr);
        } else if (a.a.a.a.b.d.a.f1317c) {
            new Thread(new RunnableC0372f(this, c(mtu, bArr), baseMeshNode)).start();
        } else {
            this.f.sendPdu(baseMeshNode, b(mtu, bArr));
        }
    }

    public C0378l(Context context, s sVar, byte[] bArr) {
        this(context, sVar, bArr, null);
    }

    public byte[] b() {
        return this.e;
    }

    public Map<Integer, ProvisionedMeshNode> c() {
        return this.f2191b;
    }

    public s d() {
        return this.f2192c;
    }

    public void g() {
        o oVar = this.g;
        if (oVar != null) {
            oVar.c();
        }
    }

    public C0378l(Context context, s sVar, byte[] bArr, InterfaceC0367a interfaceC0367a) {
        this.f2191b = new LinkedHashMap();
        this.e = new byte[]{7, -1};
        this.m = false;
        this.n = new Handler(Looper.getMainLooper());
        this.f2193d = context;
        this.f2192c = sVar == null ? new s(context) : sVar;
        this.g = new o(context, this, this, interfaceC0367a);
        this.h = new b.b.a(context, this, this);
        e();
        if (bArr != null) {
            h(bArr);
        } else {
            f();
        }
    }

    @Override // b.InterfaceC0370d
    public void b(ProvisionedMeshNode provisionedMeshNode) {
        if (provisionedMeshNode != null) {
            int unicastAddressInt = AddressUtils.getUnicastAddressInt(provisionedMeshNode.getUnicastAddress());
            d(provisionedMeshNode);
            this.f2191b.remove(Integer.valueOf(unicastAddressInt));
        }
    }

    @Override // b.InterfaceC0368b
    public void c(ProvisionedMeshNode provisionedMeshNode) {
        this.f2191b.put(Integer.valueOf(AddressUtils.getUnicastAddressInt(provisionedMeshNode.getUnicastAddress())), provisionedMeshNode);
        e(provisionedMeshNode);
        f(provisionedMeshNode);
    }

    public final void d(ProvisionedMeshNode provisionedMeshNode) {
        if (this.m) {
            SharedPreferences.Editor editorEdit = this.f2193d.getSharedPreferences(Utils.PROVISIONED_NODES_FILE, 0).edit();
            editorEdit.remove(MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), true));
            editorEdit.apply();
        }
    }

    public void a(InterfaceC0379m interfaceC0379m) {
        this.f = interfaceC0379m;
    }

    public void a(p pVar) {
        this.g.a(pVar);
    }

    public final void f(ProvisionedMeshNode provisionedMeshNode) {
        if (this.m) {
            SharedPreferences.Editor editorEdit = this.f2193d.getSharedPreferences(Utils.PROVISIONED_NODES_FILE, 0).edit();
            editorEdit.putString(MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), true), JSON.toJSONString(provisionedMeshNode, SerializerFeature.WriteClassName, SerializerFeature.WriteNullListAsEmpty));
            editorEdit.apply();
        }
    }

    public void a(q qVar) {
        this.h.a(qVar);
    }

    public final byte[] b(int i, byte[] bArr) {
        int iMin;
        int i2 = i - 1;
        int length = (bArr.length + i2) / i;
        byte b2 = bArr[0];
        if (length <= 1) {
            return bArr;
        }
        byte[] bArr2 = new byte[(bArr.length + length) - 1];
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            if (i5 == 0) {
                iMin = Math.min(bArr.length - i3, i);
                System.arraycopy(bArr, i3, bArr2, i4, iMin);
                bArr2[0] = (byte) (b2 | Constants.CMD_TYPE.CMD_DEV_LOG_NOTIFY);
            } else if (i5 == length - 1) {
                iMin = Math.min(bArr.length - i3, i);
                bArr2[i4] = (byte) (b2 | 192);
                System.arraycopy(bArr, i3, bArr2, i4 + 1, iMin);
            } else {
                iMin = Math.min(bArr.length - i3, i2);
                bArr2[i4] = (byte) (b2 | 128);
                System.arraycopy(bArr, i3, bArr2, i4 + 1, iMin);
            }
            i3 += iMin;
            i4 += i;
        }
        return bArr2;
    }

    public final boolean i(byte[] bArr) {
        int i = (bArr[0] & 192) >> 6;
        return i == 1 || i == 2 || i == 3;
    }

    public void a(InterfaceC0367a interfaceC0367a) {
        this.g.a(interfaceC0367a);
    }

    public final List<byte[]> c(int i, byte[] bArr) {
        int iMin;
        ArrayList arrayList = new ArrayList();
        int i2 = i - 1;
        int length = (bArr.length + i2) / i;
        byte b2 = bArr[0];
        if (length > 1) {
            byte[] bArr2 = new byte[(bArr.length + length) - 1];
            int i3 = 0;
            int i4 = 0;
            for (int i5 = 0; i5 < length; i5++) {
                if (i5 == 0) {
                    iMin = Math.min(bArr.length - i3, i);
                    System.arraycopy(bArr, i3, bArr2, i4, iMin);
                    bArr2[0] = (byte) (b2 | Constants.CMD_TYPE.CMD_DEV_LOG_NOTIFY);
                    arrayList.add(Arrays.copyOfRange(bArr2, 0, iMin));
                } else if (i5 == length - 1) {
                    iMin = Math.min(bArr.length - i3, i);
                    bArr2[i4] = (byte) (b2 | 192);
                    System.arraycopy(bArr, i3, bArr2, i4 + 1, iMin);
                    arrayList.add(Arrays.copyOfRange(bArr2, i4, i4 + iMin + 1));
                } else {
                    iMin = Math.min(bArr.length - i3, i2);
                    bArr2[i4] = (byte) (b2 | 128);
                    System.arraycopy(bArr, i3, bArr2, i4 + 1, iMin);
                    arrayList.add(Arrays.copyOfRange(bArr2, i4, i4 + iMin + 1));
                }
                i3 += iMin;
                i4 += i;
            }
            return arrayList;
        }
        arrayList.add(bArr);
        return arrayList;
    }

    public final List<Integer> a(Map<String, ?> map) {
        Set<String> setKeySet = map.keySet();
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = setKeySet.iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(Integer.decode(it.next()).intValue()));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public final byte[] d(int i, byte[] bArr) {
        int iMin;
        int length = (bArr.length + (i - 1)) / i;
        if (length <= 1) {
            return bArr;
        }
        int i2 = length - 1;
        byte[] bArr2 = new byte[bArr.length - i2];
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            if (i5 == 0) {
                iMin = Math.min(bArr2.length - i4, i);
                System.arraycopy(bArr, i3, bArr2, i4, iMin);
                bArr2[0] = (byte) (bArr2[0] & 63);
            } else if (i5 == i2) {
                iMin = Math.min(bArr2.length - i4, i);
                System.arraycopy(bArr, i3 + 1, bArr2, i4, iMin);
            } else {
                iMin = Math.min(bArr2.length - i4, i) - 1;
                System.arraycopy(bArr, i3 + 1, bArr2, i4, iMin);
            }
            i3 += i;
            i4 += iMin;
        }
        return bArr2;
    }

    public final void h() {
        if (this.m) {
            SharedPreferences.Editor editorEdit = this.f2193d.getSharedPreferences(Utils.PROVISIONED_NODES_FILE, 0).edit();
            Iterator<Map.Entry<Integer, ProvisionedMeshNode>> it = this.f2191b.entrySet().iterator();
            while (it.hasNext()) {
                ProvisionedMeshNode value = it.next().getValue();
                editorEdit.putString(MeshParserUtils.bytesToHex(value.getUnicastAddress(), true), JSON.toJSONString(value, SerializerFeature.WriteClassName));
            }
            editorEdit.apply();
        }
    }

    public boolean f(byte[] bArr) {
        return bArr != null && bArr[0] == 1;
    }

    public final void a(BaseMeshNode baseMeshNode, int i, byte[] bArr, a.a.a.a.b.h.a aVar) {
        if (bArr == null) {
            return;
        }
        if (i(bArr) && (bArr = a(bArr)) == null) {
            return;
        }
        a(baseMeshNode, bArr, aVar);
    }

    public final void e(ProvisionedMeshNode provisionedMeshNode) {
        int unicastAddressInt = provisionedMeshNode.getUnicastAddressInt() + provisionedMeshNode.getNumberOfElements();
        byte[] bArr = this.e;
        if (unicastAddressInt == ((bArr[1] & 255) | ((bArr[0] & 255) << 8))) {
            unicastAddressInt++;
        }
        this.f2192c.e(unicastAddressInt);
    }

    public final void a(BaseMeshNode baseMeshNode, byte[] bArr, a.a.a.a.b.h.a aVar) {
        switch (bArr[0]) {
            case 0:
                if (baseMeshNode instanceof ProvisionedMeshNode) {
                    a.a.a.a.b.m.a.a(f2190a, "Received network pdu: " + MeshParserUtils.bytesToHex(bArr, true));
                    this.h.a((ProvisionedMeshNode) baseMeshNode, bArr, aVar);
                    break;
                }
                break;
            case 1:
                a.a.a.a.b.m.a.a(f2190a, "Received mesh beacon: " + MeshParserUtils.bytesToHex(bArr, true));
                break;
            case 2:
                a.a.a.a.b.m.a.a(f2190a, "Received proxy configuration message: " + MeshParserUtils.bytesToHex(bArr, true));
                break;
            case 3:
                a.a.a.a.b.m.a.a(f2190a, "Received provisioning message: " + MeshParserUtils.bytesToHex(bArr, true));
                this.g.b((UnprovisionedMeshNode) baseMeshNode, bArr);
                break;
            default:
                a.a.a.a.b.m.a.d(f2190a, "Unknown pdu received: " + MeshParserUtils.bytesToHex(bArr, true));
                if (baseMeshNode instanceof UnprovisionedMeshNode) {
                    this.g.d((UnprovisionedMeshNode) baseMeshNode, bArr);
                }
                break;
        }
    }

    public final byte[] e(byte[] bArr) {
        if (bArr == null || bArr.length <= 8) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 9, 8);
        return byteBufferOrder.array();
    }

    public String b(byte[] bArr) {
        return MeshParserUtils.bytesToHex(SecureUtils.calculateK3(bArr), false);
    }

    public final byte[] d(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 1, 8);
        return byteBufferOrder.array();
    }

    public boolean b(ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        byte[] bArrE;
        byte[] bArrC = c(bArr);
        if (bArrC == null || (bArrE = e(bArr)) == null) {
            return false;
        }
        boolean zEquals = Arrays.equals(bArrC, SecureUtils.calculateHash(provisionedMeshNode.getIdentityKey(), bArrE, provisionedMeshNode.getUnicastAddress()));
        if (zEquals) {
            provisionedMeshNode.setNodeIdentifier(MeshParserUtils.bytesToHex(bArrC, false));
        }
        return zEquals;
    }

    public void b(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, byte[] bArr2, int i) {
        this.h.b(provisionedMeshNode, 0, bArr, bArr2, i);
    }

    public final byte[] c(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 1, 8);
        return byteBufferOrder.array();
    }

    public final void b(ProvisionedMeshNode provisionedMeshNode, boolean z, String str, byte[] bArr, boolean z2, int i, int i2, byte[] bArr2, IActionListener iActionListener) {
        this.h.a(provisionedMeshNode, ProxyProtocolMessageType.NetworkPDU, z, str, bArr, z2, i, i2, bArr2);
        a(provisionedMeshNode.getUnicastAddress(), 13871105, new byte[]{bArr2[1], bArr2[2]}, new C0375i(this, iActionListener));
    }

    public void b(byte[] bArr, int i, byte[] bArr2, IActionListener<Object> iActionListener) {
        this.h.a(bArr, i, bArr2, iActionListener);
    }

    public final void a(BaseMeshNode baseMeshNode, int i, byte[] bArr) {
        if (i(bArr)) {
            byte[] bArrA = a(i, bArr);
            if (bArrA == null) {
                return;
            } else {
                bArr = d(i, bArrA);
            }
        }
        a(baseMeshNode, bArr);
    }

    public final void a(BaseMeshNode baseMeshNode, byte[] bArr) {
        switch (bArr[0]) {
            case 0:
                if (baseMeshNode instanceof ProvisionedMeshNode) {
                    a.a.a.a.b.m.a.a(f2190a, "Network pdu sent: " + MeshParserUtils.bytesToHex(bArr, true));
                    this.h.a((ProvisionedMeshNode) baseMeshNode, bArr);
                    break;
                }
                break;
            case 1:
                a.a.a.a.b.m.a.a(f2190a, "Mesh beacon pdu sent: " + MeshParserUtils.bytesToHex(bArr, true));
                break;
            case 2:
                a.a.a.a.b.m.a.a(f2190a, "Proxy configuration pdu sent: " + MeshParserUtils.bytesToHex(bArr, true));
                break;
            case 3:
                a.a.a.a.b.m.a.a(f2190a, "Provisioning pdu sent: " + MeshParserUtils.bytesToHex(bArr, true));
                this.g.a((UnprovisionedMeshNode) baseMeshNode);
                break;
            default:
                a.a.a.a.b.m.a.d(f2190a, "Unknown pdu sent: " + MeshParserUtils.bytesToHex(bArr, true));
                break;
        }
    }

    @Override // b.InterfaceC0370d
    public void a(ProvisionedMeshNode provisionedMeshNode) {
        if (provisionedMeshNode != null) {
            int unicastAddressInt = AddressUtils.getUnicastAddressInt(provisionedMeshNode.getUnicastAddress());
            a.a.a.a.b.m.a.c(f2190a, "updateMeshNode: unicast " + unicastAddressInt);
            this.f2191b.put(Integer.valueOf(unicastAddressInt), provisionedMeshNode);
            f(provisionedMeshNode);
        }
    }

    public final byte[] a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = (bArr[0] & 192) >> 6;
        if (i == 1) {
            int length = bArr.length;
            this.j = 0;
            this.j += length;
            this.i = bArr;
            this.i[0] = (byte) (bArr[0] & 63);
        } else {
            if ((i != 2 && i != 3) || this.i == null) {
                return null;
            }
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 1, bArr.length);
            int length2 = bArrCopyOfRange.length;
            byte[] bArr2 = this.i;
            byte[] bArr3 = new byte[bArr2.length + length2];
            System.arraycopy(bArr2, 0, bArr3, 0, this.j);
            System.arraycopy(bArrCopyOfRange, 0, bArr3, this.j, length2);
            this.j += length2;
            this.i = bArr3;
            if (i == 3) {
                byte[] bArr4 = this.i;
                this.i = null;
                return bArr4;
            }
        }
        return null;
    }

    public final byte[] a(int i, byte[] bArr) {
        if (this.k == null) {
            int iMin = Math.min(bArr.length, i);
            this.l = 0;
            this.l += iMin;
            this.k = bArr;
        } else {
            int iMin2 = Math.min(bArr.length, i);
            byte[] bArr2 = this.k;
            byte[] bArr3 = new byte[bArr2.length + iMin2];
            System.arraycopy(bArr2, 0, bArr3, 0, this.l);
            System.arraycopy(bArr, 0, bArr3, this.l, iMin2);
            this.l += iMin2;
            this.k = bArr3;
            if (iMin2 < i) {
                byte[] bArr4 = this.k;
                this.k = null;
                return bArr4;
            }
        }
        return null;
    }

    public void a(@NonNull String str, String str2, byte[] bArr, UnprovisionedMeshNodeData unprovisionedMeshNodeData, a.a.a.a.b.i.J j) {
        this.g.a(str, str2, this.f2192c.h(), this.f2192c.g(), this.f2192c.d(), this.f2192c.f(), this.f2192c.i(), this.f2192c.e(), this.e, bArr, unprovisionedMeshNodeData, j);
    }

    public void a(@NonNull UnprovisionedMeshNode unprovisionedMeshNode) {
        this.g.g(unprovisionedMeshNode);
    }

    public boolean a(String str, byte[] bArr) {
        byte[] bArrD = d(bArr);
        return bArrD != null && str.equals(MeshParserUtils.bytesToHex(bArrD, false).toUpperCase());
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, int i, String str) {
        if (str != null && !str.isEmpty()) {
            this.h.a(provisionedMeshNode, i, str, 0);
            return;
        }
        throw new IllegalArgumentException(this.f2193d.getString(R.string.error_null_key));
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, MeshModel meshModel, int i) {
        this.h.a(provisionedMeshNode, 0, bArr, meshModel.getModelId(), i);
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, int i, int i2) {
        this.h.a(provisionedMeshNode, 0, bArr, i, i2);
    }

    public void a(ConfigModelPublicationSetParams configModelPublicationSetParams) {
        this.h.a(configModelPublicationSetParams);
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, byte[] bArr2, int i) {
        this.h.a(provisionedMeshNode, 0, bArr, bArr2, i);
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, int i, byte[] bArr) {
        a.a.a.a.b.m.a.a(f2190a, "setProxyFilterType, paramter: " + ConvertUtils.bytes2HexString(bArr));
        if (provisionedMeshNode != null) {
            byte[] bArr2 = {0, 0};
            Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
            if (addedAppKeys == null || addedAppKeys.size() <= 0) {
                return;
            }
            int iIntValue = addedAppKeys.keySet().iterator().next().intValue();
            this.h.a(provisionedMeshNode, ProxyProtocolMessageType.ProxyConfiguration, false, provisionedMeshNode.getAddedAppKeys().get(Integer.valueOf(iIntValue)), bArr2, false, iIntValue, i, bArr);
        }
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        a.a.a.a.b.m.a.a(f2190a, "addAddressToFilter, addAddressParameter: " + ConvertUtils.bytes2HexString(bArr));
        if (provisionedMeshNode != null) {
            byte[] bArr2 = {0, 0};
            Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
            if (addedAppKeys == null || addedAppKeys.size() <= 0) {
                return;
            }
            int iIntValue = addedAppKeys.keySet().iterator().next().intValue();
            this.h.a(provisionedMeshNode, ProxyProtocolMessageType.ProxyConfiguration, false, provisionedMeshNode.getAddedAppKeys().get(Integer.valueOf(iIntValue)), bArr2, false, iIntValue, 1, bArr);
        }
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, boolean z, String str, byte[] bArr, boolean z2, int i, int i2, byte[] bArr2) {
        this.h.a(provisionedMeshNode, ProxyProtocolMessageType.NetworkPDU, z, str, bArr, z2, i, i2, bArr2);
    }

    public boolean a(ProvisionedMeshNode provisionedMeshNode, boolean z, byte[] bArr, int i, byte[] bArr2) {
        if (provisionedMeshNode == null || provisionedMeshNode.getAddedAppKeys() == null) {
            return false;
        }
        this.h.a(provisionedMeshNode, ProxyProtocolMessageType.NetworkPDU, z, provisionedMeshNode.getAddedAppKeys().get(0), bArr, false, 0, i, bArr2);
        return true;
    }

    public <T> void a(ProvisionedMeshNode provisionedMeshNode, String str, byte[] bArr, int i, int i2, byte[] bArr2, IActionListener<T> iActionListener) {
        if (provisionedMeshNode != null && provisionedMeshNode.getAddedAppKeys() != null) {
            String str2 = provisionedMeshNode.getAddedAppKeys().get(0);
            new a(bArr, i2, bArr2, b.c.a.b().c(provisionedMeshNode.getNetworkKey(), bArr), new C0373g(this), iActionListener).a();
            this.h.a(provisionedMeshNode, ProxyProtocolMessageType.NetworkPDU, true, TextUtils.isEmpty(str) ? str2 : str, bArr, false, 0, i, bArr2);
            return;
        }
        Utils.notifyFailed(iActionListener, -7, "Can not found target device or empty appKeys");
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, boolean z, String str, byte[] bArr, boolean z2, int i, int i2, byte[] bArr2, IActionListener iActionListener) {
        b(provisionedMeshNode, z, str, bArr, z2, i, i2, bArr2, iActionListener);
        this.o = new RunnableC0374h(this, provisionedMeshNode, bArr2, iActionListener, z, str, bArr, z2, i, i2);
        this.n.postDelayed(this.o, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
    }

    public void a(byte[] bArr, int i, IActionListener<Object> iActionListener) {
        this.h.a(bArr, i, (byte[]) null, iActionListener);
    }

    public void a(byte[] bArr, int i, byte[] bArr2, IActionListener<Object> iActionListener) {
        this.h.a(bArr, i, bArr2, iActionListener);
    }
}

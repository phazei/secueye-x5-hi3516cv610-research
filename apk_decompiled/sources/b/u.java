package b;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import b.K;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.ConnectionParams;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import datasource.bean.SigmeshKey;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: SIGMeshNetwork.java */
/* JADX INFO: loaded from: classes.dex */
public class u {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public volatile ConcurrentHashMap<String, a> f2208a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public y f2209b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, ProvisionedMeshNode> f2210c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Map<String, ProvisionedMeshNode> f2211d;
    public a e;
    public byte[] f;
    public List<SigmeshKey> g;
    public volatile boolean h = false;

    /* JADX INFO: compiled from: SIGMeshNetwork.java */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public boolean f2212a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public byte[] f2213b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public SparseArray<byte[]> f2214c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public byte[] f2215d;
        public SparseArray<ProvisionedMeshNode> e;
        public K f;
        public byte[] g;
        public String h;
        public byte i;

        /* JADX INFO: renamed from: b.u$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: SIGMeshNetwork.java */
        public static final class C0179a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public boolean f2216a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public byte[] f2217b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public SparseArray<byte[]> f2218c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public byte[] f2219d;

            public C0179a a(boolean z) {
                this.f2216a = z;
                return this;
            }

            public C0179a b(byte[] bArr) {
                if (bArr != null && bArr.length == 16) {
                    this.f2217b = bArr;
                    return this;
                }
                throw new IllegalArgumentException("unknown networkKey " + bArr);
            }

            public C0179a a(SparseArray<byte[]> sparseArray) {
                this.f2218c = sparseArray;
                return this;
            }

            public C0179a a(byte[] bArr) {
                this.f2219d = bArr;
                return this;
            }

            public a a() {
                return new a(this.f2216a, this.f2217b, this.f2218c, this.f2219d, null);
            }
        }

        public /* synthetic */ a(boolean z, byte[] bArr, SparseArray sparseArray, byte[] bArr2, t tVar) {
            this(z, bArr, sparseArray, bArr2);
        }

        public byte[] d() {
            return this.f2213b;
        }

        public K e() {
            return this.f;
        }

        public boolean f() {
            if (this.e.size() == 0) {
                return true;
            }
            if (!i()) {
                K k = this.f;
                return k != null && k.f() == 2;
            }
            K k2 = this.f;
            if (k2 == null || k2.f() != 2) {
                return this.f != null && MeshDeviceInfoManager.getInstance().isOnyTinyMeshExistInCurrentUser();
            }
            return true;
        }

        public boolean g() {
            if (this.e.size() == 0) {
                return true;
            }
            K k = this.f;
            return k != null && (k.f() == 2 || this.f.f() == 1);
        }

        public boolean h() {
            K k = this.f;
            return k != null ? k.l() : this.f2212a;
        }

        public boolean i() {
            return this.f2212a;
        }

        public boolean j() {
            if (this.e.size() == 0) {
                return true;
            }
            K k = this.f;
            return k != null && k.f() == 2;
        }

        public String toString() {
            return "Subnets{mPrimaryFlag=" + this.f2212a + ", mNetworkKey=" + MeshParserUtils.bytesToHex(this.f2213b, false) + '}';
        }

        public a(boolean z, byte[] bArr, SparseArray<byte[]> sparseArray, byte[] bArr2) {
            this.e = new SparseArray<>();
            this.f2212a = z;
            this.f2213b = bArr;
            this.f2214c = sparseArray;
            this.f2215d = bArr2;
            this.h = MeshParserUtils.bytesToHex(SecureUtils.calculateK3(this.f2213b), false);
            this.i = SecureUtils.calculateK2(this.f2213b, SecureUtils.K2_MASTER_INPUT).getNid();
            a.a.a.a.b.m.a.c("SIGMeshNetwork", String.format("%s, networkID: %s", this, this.h));
        }

        public void a(K k) {
            this.f = k;
            this.f.b(toString());
        }

        public BaseMeshNode b() {
            if (this.e.size() <= 0) {
                return null;
            }
            SparseArray<ProvisionedMeshNode> sparseArray = this.e;
            return sparseArray.get(sparseArray.keyAt(0));
        }

        public byte c() {
            return this.i;
        }

        public BaseMeshNode d(byte[] bArr) {
            if (bArr == null || bArr.length != 2) {
                return null;
            }
            return a(Integer.parseInt(MeshParserUtils.bytesToHex(bArr, false), 16));
        }

        public void e(byte[] bArr) {
            this.g = bArr;
        }

        public ProvisionedMeshNode c(byte[] bArr) {
            for (int i = 0; i < this.e.size(); i++) {
                ProvisionedMeshNode provisionedMeshNode = this.e.get(this.e.keyAt(i));
                if (u.a(provisionedMeshNode, bArr)) {
                    return provisionedMeshNode;
                }
            }
            return null;
        }

        public void a(boolean z) {
            this.f2212a = z;
        }

        public boolean b(byte[] bArr) {
            byte[] bArrF = u.f(bArr);
            if (TextUtils.isEmpty(this.h)) {
                this.h = MeshParserUtils.bytesToHex(SecureUtils.calculateK3(this.f2213b), false);
            }
            return bArrF != null && this.h.equals(MeshParserUtils.bytesToHex(bArrF, false).toUpperCase());
        }

        public SparseArray<byte[]> a() {
            return this.f2214c;
        }

        public BaseMeshNode a(String str) {
            for (int i = 0; i < this.e.size(); i++) {
                ProvisionedMeshNode provisionedMeshNode = this.e.get(this.e.keyAt(i));
                if (provisionedMeshNode == null || TextUtils.isEmpty(provisionedMeshNode.getBluetoothDeviceAddress()) || provisionedMeshNode.getBluetoothDeviceAddress().equals(str)) {
                    return provisionedMeshNode;
                }
            }
            return null;
        }

        public BaseMeshNode a(int i) {
            ProvisionedMeshNode provisionedMeshNode = this.e.get(i);
            a.a.a.a.b.m.a.a("SIGMeshNetwork", String.format("Get mesh node via unicast address(%d) result: %s", Integer.valueOf(i), provisionedMeshNode));
            return provisionedMeshNode == null ? a(AddressUtils.getUnicastAddressBytes(i)) : provisionedMeshNode;
        }

        public void a(ProvisionedMeshNode provisionedMeshNode, boolean z) {
            Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
            int size = addedAppKeys == null ? 0 : addedAppKeys.size();
            ProvisionedMeshNode provisionedMeshNode2 = this.e.get(Integer.parseInt(MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), false), 16));
            if (!z && provisionedMeshNode2 != null && provisionedMeshNode2.getAddedAppKeys() != null && provisionedMeshNode2.getAddedAppKeys().size() >= size) {
                if (provisionedMeshNode.getDeviceKey() != null) {
                    a.a.a.a.b.m.a.a("SIGMeshNetwork", "replace deviceKey with latest node");
                    provisionedMeshNode2.setDeviceKey(provisionedMeshNode.getDeviceKey());
                    return;
                }
                return;
            }
            if (addedAppKeys != null && addedAppKeys.size() <= 0 && this.f2214c.get(0) != null) {
                a.a.a.a.b.m.a.d("SIGMeshNetwork", "set added default appkey for node");
                provisionedMeshNode.setAddedAppKey(0, MeshParserUtils.bytesToHex(this.f2214c.get(0), false));
            }
            String devId = provisionedMeshNode.getDevId();
            if (a.a.a.a.b.d.a.f1316b && addedAppKeys.size() == 1 && AliMeshUUIDParserUtil.isSupportAutomaticallyGenerateShareAppKey(devId)) {
                String str = provisionedMeshNode.getAddedAppKeys().get(0);
                if (!TextUtils.isEmpty(str)) {
                    byte[] networkKey = provisionedMeshNode.getNetworkKey();
                    String lowerCase = AliMeshUUIDParserUtil.extractMacAddressFromUUID(devId).replace(":", "").toLowerCase();
                    String str2 = String.format("%s,%s,%s", str.toLowerCase(), MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), false).toLowerCase(), MeshParserUtils.bytesToHex(networkKey, false).toLowerCase());
                    String strBytesToHex = MeshParserUtils.bytesToHex(Arrays.copyOf(SecureUtils.calculateSha256(str2.getBytes()), 16), false);
                    provisionedMeshNode.setAddedAppKey(1, strBytesToHex);
                    a.a.a.a.b.m.a.a("SIGMeshNetwork", String.format("Automatically generate AppKey1, input[%s], result[%s] for node[%s]", str2, strBytesToHex, lowerCase));
                }
            }
            this.e.put(Integer.parseInt(MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), false), 16), provisionedMeshNode);
        }

        public final ProvisionedMeshNode a(byte[] bArr) {
            UnprovisionedMeshNode unprovisionedMeshNode = new UnprovisionedMeshNode();
            unprovisionedMeshNode.setConfigurationSrc(this.g);
            unprovisionedMeshNode.setUnicastAddress(bArr);
            unprovisionedMeshNode.setIvIndex(this.f2215d);
            unprovisionedMeshNode.setNetworkKey(this.f2213b);
            ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(unprovisionedMeshNode);
            provisionedMeshNode.setIsProvisioned(true);
            provisionedMeshNode.setConfigured(true);
            if (this.f2214c.get(0) != null) {
                String strBytesToHex = MeshParserUtils.bytesToHex(this.f2214c.get(0), false);
                a.a.a.a.b.m.a.d("SIGMeshNetwork", "set added default appKey:" + strBytesToHex + " for node");
                provisionedMeshNode.setAddedAppKey(0, strBytesToHex);
            }
            a(provisionedMeshNode, false);
            return provisionedMeshNode;
        }
    }

    public void b(K.c cVar) {
        y yVar = this.f2209b;
        if (yVar == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
        } else {
            yVar.b(cVar);
        }
    }

    public void c(byte[] bArr) {
        if (this.f2208a == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "netKey to subnets mapping data is not initialized");
            return;
        }
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr, false);
        a aVar = this.f2208a.get(strBytesToHex);
        if (aVar == null) {
            a.a.a.a.b.m.a.d("SIGMeshNetwork", "Can't find the corresponding subnet");
            return;
        }
        y yVar = this.f2209b;
        if (yVar != null) {
            yVar.b(aVar);
        }
        if (aVar.e() != null) {
            aVar.e().n();
        }
        this.f2208a.remove(strBytesToHex);
    }

    public a d() {
        return this.e;
    }

    public void e() {
        this.h = true;
        y yVar = this.f2209b;
        if (yVar != null) {
            yVar.b();
        }
    }

    public void f() {
        a.a.a.a.b.m.a.c("SIGMeshNetwork", "Try connect all subnets");
        if (this.f2209b == null || this.f2208a == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
            return;
        }
        for (Map.Entry<String, a> entry : this.f2208a.entrySet()) {
            if (entry.getValue().e() == null || entry.getValue().h()) {
                this.f2209b.f();
                return;
            }
        }
        a.a.a.a.b.m.a.a("SIGMeshNetwork", "All SIGMesh subnets in connected state, no need to perform connect");
    }

    public void g() {
        y yVar = this.f2209b;
        if (yVar == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
        } else {
            yVar.g();
        }
    }

    public void h() {
        this.h = false;
        y yVar = this.f2209b;
        if (yVar != null) {
            yVar.h();
        }
    }

    public void i(byte[] bArr) {
        this.f = bArr;
    }

    public void a(Context context, q qVar) {
        if (this.f2209b == null) {
            this.f2209b = new y(context, this, qVar);
        }
        if (this.h) {
            this.f2209b.b();
        } else {
            this.f2209b.h();
        }
    }

    public a d(byte[] bArr) {
        byte[] bArrF = f(bArr);
        for (Map.Entry<String, a> entry : this.f2208a.entrySet()) {
            String str = entry.getValue().h;
            if (bArrF != null && str.equals(MeshParserUtils.bytesToHex(bArrF, false).toUpperCase())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static byte[] e(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 1, 8);
        return byteBufferOrder.array();
    }

    public static byte[] g(byte[] bArr) {
        if (bArr == null || bArr.length <= 8) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 9, 8);
        return byteBufferOrder.array();
    }

    public void b(List<SigmeshKey> list) {
        this.g = list;
    }

    public a h(byte[] bArr) {
        if (this.f2208a == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "netKey to subnets mapping data is not initialized");
            return null;
        }
        return this.f2208a.get(MeshParserUtils.bytesToHex(bArr, false));
    }

    public ProvisionedMeshNode b(String str) {
        Map<String, ProvisionedMeshNode> map = this.f2210c;
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        return this.f2210c.get(str);
    }

    public void a(a aVar, boolean z) {
        a.a.a.a.b.m.a.c("SIGMeshNetwork", "Try connect specified " + aVar);
        if (this.f2209b != null && this.f2208a != null) {
            if (aVar.e() != null && !aVar.h()) {
                a.a.a.a.b.m.a.d("SIGMeshNetwork", "not need connect, return");
                return;
            } else {
                this.f2209b.a(aVar, z);
                return;
            }
        }
        a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
    }

    public Pair<a, ProvisionedMeshNode> b(byte[] bArr) {
        for (Map.Entry<String, a> entry : this.f2208a.entrySet()) {
            for (int i = 0; i < entry.getValue().e.size(); i++) {
                ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) entry.getValue().e.get(entry.getValue().e.keyAt(i));
                if (a(provisionedMeshNode, bArr)) {
                    return new Pair<>(entry.getValue(), provisionedMeshNode);
                }
            }
        }
        return null;
    }

    public static byte[] f(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 1, 8);
        return byteBufferOrder.array();
    }

    public a c(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Map<String, ProvisionedMeshNode> map = this.f2210c;
        if (map == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "devId to mesh node mapping data is not initialized");
            return null;
        }
        ProvisionedMeshNode provisionedMeshNode = map.get(str);
        if (provisionedMeshNode == null || provisionedMeshNode.getNetworkKey() == null) {
            return null;
        }
        return h(provisionedMeshNode.getNetworkKey());
    }

    public byte[] b() {
        return this.f;
    }

    public void a(ConnectionParams connectionParams) {
        if (connectionParams == null) {
            f();
            return;
        }
        if (!TextUtils.isEmpty(connectionParams.getDeviceId())) {
            a.a.a.a.b.m.a.c("SIGMeshNetwork", "Try connect target subnets, first find it...");
            a aVarC = c(connectionParams.getDeviceId());
            if (aVarC == null) {
                a.a.a.a.b.m.a.b("SIGMeshNetwork", String.format("The target subnet was not found by deviceId(%s)", connectionParams.getDeviceId()));
                return;
            }
            if (aVarC.f()) {
                a.a.a.a.b.m.a.c("SIGMeshNetwork", String.format("The %s is available, do nothing", aVarC));
                return;
            }
            y yVar = this.f2209b;
            if (yVar == null) {
                a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
                return;
            } else {
                yVar.a(aVarC, false);
                return;
            }
        }
        if (TextUtils.isEmpty(connectionParams.getDirectConnectionProxyNodeMacAddress())) {
            return;
        }
        String directConnectionProxyNodeMacAddress = connectionParams.getDirectConnectionProxyNodeMacAddress();
        if (!BluetoothAdapter.checkBluetoothAddress(directConnectionProxyNodeMacAddress)) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", directConnectionProxyNodeMacAddress + " is not a valid Bluetooth address");
            return;
        }
        a aVar = this.e;
        if (aVar == null || aVar.e() == null) {
            return;
        }
        if (this.e.a(directConnectionProxyNodeMacAddress) == null) {
            this.e.b();
        }
        this.e.e().b(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(directConnectionProxyNodeMacAddress));
    }

    public int c() {
        if (this.f2208a == null) {
            return 0;
        }
        return this.f2208a.size();
    }

    public void a(K.c cVar) {
        y yVar = this.f2209b;
        if (yVar == null) {
            a.a.a.a.b.m.a.b("SIGMeshNetwork", "Must initTransportManager first");
        } else {
            yVar.a(cVar);
        }
    }

    public boolean a() {
        a.a.a.a.b.m.a.c("SIGMeshNetwork", "Check any subnet accept new connections ...");
        for (Map.Entry<String, a> entry : this.f2208a.entrySet()) {
            a value = entry.getValue();
            if (value != null && (!value.g() || value.h())) {
                a.a.a.a.b.m.a.a("SIGMeshNetwork", String.format("%s is disconnected or multiProxy acceptable", entry.getValue()));
                return true;
            }
            a.a.a.a.b.m.a.a("SIGMeshNetwork", String.format("%s is connected", entry.getValue()));
        }
        return false;
    }

    public void a(boolean z, MeshService.OnDisconnectListener onDisconnectListener) {
        if (z) {
            a aVar = this.e;
            if (aVar != null && aVar.f != null) {
                this.e.f.a(onDisconnectListener);
                return;
            } else {
                onDisconnectListener.onDisconnected();
                return;
            }
        }
        if (this.f2208a == null) {
            return;
        }
        Iterator<Map.Entry<String, a>> it = this.f2208a.entrySet().iterator();
        while (it.hasNext()) {
            a value = it.next().getValue();
            if (value.f != null) {
                value.f.a(new t(this));
            }
        }
    }

    public void a(byte[] bArr, a aVar, boolean z) {
        if (bArr == null || aVar == null) {
            return;
        }
        aVar.e(this.f);
        if (this.f2208a == null) {
            synchronized (this) {
                this.f2208a = new ConcurrentHashMap<>();
            }
        }
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr, false);
        if (this.f2208a.containsKey(strBytesToHex) && z) {
            a.a.a.a.b.m.a.c("SIGMeshNetwork", String.format("Update %s", aVar));
            this.f2208a.put(strBytesToHex, aVar);
            if (aVar.i()) {
                this.e = aVar;
                return;
            }
            return;
        }
        if (this.f2208a.containsKey(strBytesToHex)) {
            return;
        }
        a.a.a.a.b.m.a.c("SIGMeshNetwork", String.format("add %s", aVar));
        this.f2208a.put(strBytesToHex, aVar);
        if (aVar.i()) {
            this.e = aVar;
        }
    }

    public void a(ProvisionedMeshNode provisionedMeshNode, boolean z, boolean z2) {
        if (provisionedMeshNode == null || provisionedMeshNode.getNetworkKey() == null) {
            return;
        }
        provisionedMeshNode.setIsProvisioned(true);
        provisionedMeshNode.setConfigurationSrc(this.f);
        if (this.f2208a == null) {
            synchronized (this) {
                this.f2208a = new ConcurrentHashMap<>();
            }
        }
        String devId = provisionedMeshNode.getDevId();
        if (!TextUtils.isEmpty(devId)) {
            if (this.f2210c == null) {
                this.f2210c = new HashMap();
            }
            if (this.f2211d == null) {
                this.f2211d = new HashMap();
            }
            this.f2210c.put(devId, provisionedMeshNode);
            this.f2211d.put(Utils.deviceId2Mac(devId).toLowerCase(), provisionedMeshNode);
        }
        a.a.a.a.b.m.a.c("SIGMeshNetwork", String.format(Locale.getDefault(), "Add or update provisioned mesh node(%s) to network (address:%d), appKeys count: %d", provisionedMeshNode.getDevId(), Integer.valueOf(Integer.parseInt(MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), false), 16)), Integer.valueOf(provisionedMeshNode.getAddedAppKeys() == null ? 0 : provisionedMeshNode.getAddedAppKeys().size())));
        a aVarA = this.f2208a.get(MeshParserUtils.bytesToHex(provisionedMeshNode.getNetworkKey(), false));
        if (aVarA == null) {
            Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
            SparseArray<byte[]> sparseArray = new SparseArray<>();
            for (Integer num : addedAppKeys.keySet()) {
                sparseArray.put(num.intValue(), MeshParserUtils.toByteArray(addedAppKeys.get(num)));
            }
            aVarA = new a.C0179a().b(provisionedMeshNode.getNetworkKey()).a(new byte[]{0, 0, 0, 0}).a(z).a(sparseArray).a();
            a(provisionedMeshNode.getNetworkKey(), aVarA, true);
        }
        aVarA.a(provisionedMeshNode, z2);
    }

    public BaseMeshNode a(byte[] bArr, byte[] bArr2) {
        if (bArr != null && bArr2 != null) {
            a aVar = this.f2208a.get(MeshParserUtils.bytesToHex(bArr, false));
            if (aVar != null) {
                return aVar.d(bArr2);
            }
        }
        return null;
    }

    public BaseMeshNode a(String str, byte[] bArr) {
        a aVar;
        if (TextUtils.isEmpty(str) || bArr == null || (aVar = this.f2208a.get(str)) == null) {
            return null;
        }
        return aVar.d(bArr);
    }

    public ProvisionedMeshNode a(String str) {
        Map<String, ProvisionedMeshNode> map = this.f2211d;
        if (map == null || !map.containsKey(str.toLowerCase())) {
            return null;
        }
        return this.f2211d.get(str.toLowerCase());
    }

    public void a(List<SigmeshKey> list) {
        if (this.f2208a == null) {
            return;
        }
        a.a.a.a.b.m.a.a("SIGMeshNetwork", "Full refresh network");
        HashSet hashSet = new HashSet();
        for (SigmeshKey sigmeshKey : list) {
            if (sigmeshKey.getProvisionNetKey() != null) {
                String netKey = sigmeshKey.getProvisionNetKey().getNetKey();
                if (!TextUtils.isEmpty(netKey)) {
                    hashSet.add(netKey.toLowerCase());
                }
            }
        }
        for (Map.Entry<String, a> entry : this.f2208a.entrySet()) {
            if (!hashSet.contains(entry.getKey().toLowerCase())) {
                c(entry.getValue().d());
            }
        }
    }

    public static boolean a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        byte[] bArrG;
        byte[] bArrE = e(bArr);
        if (bArrE == null || (bArrG = g(bArr)) == null || provisionedMeshNode.getIdentityKey() == null) {
            return false;
        }
        boolean zEquals = Arrays.equals(bArrE, SecureUtils.calculateHash(provisionedMeshNode.getIdentityKey(), bArrG, provisionedMeshNode.getUnicastAddress()));
        if (zEquals) {
            provisionedMeshNode.setNodeIdentifier(MeshParserUtils.bytesToHex(bArrE, false));
        }
        return zEquals;
    }
}

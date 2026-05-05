package b;

import a.a.a.a.b.a.C0321g;
import aisscanner.ScanRecord;
import aisscanner.ScanResult;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.u;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.R;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback;
import com.alibaba.ailabs.iot.mesh.ut.UtError;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.google.android.gms.common.ConnectionResult;
import com.xiaomi.mipush.sdk.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.ProxyCommunicationQuality;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class K implements BleMeshManagerCallbacks, FastProvisionTransportCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static String f2104a = "SubnetsBiz";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static a f2105b = new a();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static int f2106c = 100;
    public int G;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f2107d;
    public Context e;
    public boolean g;
    public C0378l h;
    public boolean i;
    public BaseMeshNode k;
    public BluetoothDevice m;
    public boolean n;
    public Handler o;
    public String p;
    public MeshService.OnDisconnectListener q;
    public c r;
    public u.a s;
    public a.a.a.a.b.k.d t;
    public C0321g u;
    public a.a.a.a.b.h.a v;
    public a.a.a.a.b.i.J w;
    public q x;
    public final Map<String, d> f = new LinkedHashMap();
    public boolean j = true;
    public int l = 0;
    public final List<String> y = new ArrayList();
    public final List<ExtendedBluetoothDevice> z = new LinkedList();
    public final List<d> A = new LinkedList();
    public final Map<String, Integer> B = new LinkedHashMap();
    public final Deque<SIGMeshBizRequest> C = new LinkedList();
    public b D = null;
    public boolean E = true;
    public boolean F = false;
    public Runnable H = null;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SubnetsBiz.java */
    static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final Queue<b> f2108a = new LinkedList();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final Handler f2109b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public Runnable f2110c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final AtomicInteger f2111d;

        /* JADX INFO: renamed from: b.K$a$a, reason: collision with other inner class name */
        /* JADX INFO: compiled from: SubnetsBiz.java */
        public interface InterfaceC0175a {
            void a(Object obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: compiled from: SubnetsBiz.java */
        class b {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public Object f2112a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            public InterfaceC0175a f2113b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            public Runnable f2114c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            public int f2115d;

            public b(Object obj, InterfaceC0175a interfaceC0175a, Runnable runnable, int i) {
                this.f2112a = obj;
                this.f2113b = interfaceC0175a;
                this.f2114c = runnable;
                this.f2115d = i;
            }
        }

        public a() {
            Looper looperMyLooper = Looper.myLooper();
            if (looperMyLooper != null) {
                this.f2109b = new Handler(looperMyLooper);
            } else {
                this.f2109b = new Handler(Looper.getMainLooper());
            }
            this.f2111d = new AtomicInteger(0);
        }

        public synchronized int a(Object obj, InterfaceC0175a interfaceC0175a, Runnable runnable) {
            boolean z = this.f2108a.size() > 0;
            int andIncrement = this.f2111d.getAndIncrement();
            b bVar = new b(obj, interfaceC0175a, runnable, andIncrement);
            this.f2108a.add(bVar);
            if (z) {
                return andIncrement;
            }
            a(bVar);
            if (interfaceC0175a != null) {
                interfaceC0175a.a(obj);
            }
            return andIncrement;
        }

        public synchronized void a(int i) {
            b bVarPeek;
            b bVarPeek2 = this.f2108a.peek();
            if (bVarPeek2 != null) {
                if (bVarPeek2.f2115d != i) {
                    return;
                }
                Runnable runnable = this.f2110c;
                if (runnable != null) {
                    this.f2109b.removeCallbacks(runnable);
                }
                try {
                    this.f2108a.remove();
                    if (!this.f2108a.isEmpty() && (bVarPeek = this.f2108a.peek()) != null && bVarPeek.f2113b != null) {
                        a(bVarPeek);
                        bVarPeek.f2113b.a(bVarPeek.f2112a);
                    }
                } catch (NoSuchElementException unused) {
                }
            }
        }

        public final void a(b bVar) {
            Handler handler = this.f2109b;
            J j = new J(this, bVar);
            this.f2110c = j;
            handler.postDelayed(j, 8000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SubnetsBiz.java */
    class b extends a.a.a.a.b.a.K {
        public String j;

        public b() {
            super(null, K.this.C);
            this.j = "" + b.class.getSimpleName();
        }

        @Override // a.a.a.a.b.a.K
        public synchronized void b() {
            if (K.this.C.size() != 0 && !this.i.get()) {
                a.a.a.a.b.m.a.a(this.j, "nextRequest called");
                this.h = true;
                new Thread(new M(this)).start();
                return;
            }
            this.h = false;
        }
    }

    /* JADX INFO: compiled from: SubnetsBiz.java */
    public interface c {
        void onConnectionStateChanged(K k, int i, int i2);

        void onMeshChannelReady(K k);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SubnetsBiz.java */
    final class d implements InterfaceC0379m {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final BleMeshManager f2116a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final C0378l f2117b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final C0321g f2118c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final BluetoothDevice f2119d;
        public long e = -1;
        public ProxyCommunicationQuality f;

        public d(BleMeshManager bleMeshManager, BluetoothDevice bluetoothDevice) {
            this.f2116a = bleMeshManager;
            this.f2119d = bluetoothDevice;
            this.f2117b = new C0378l(K.this.e);
            this.f2117b.a(this);
            this.f2117b.a(K.this.x);
            this.f2118c = new C0321g(this.f2117b);
        }

        public void e() {
            this.f2118c.c();
        }

        public void f() {
            this.f2118c.d();
        }

        @Override // b.InterfaceC0379m
        public int getMtu() {
            int mtuSize = this.f2116a.getMtuSize();
            a.a.a.a.b.m.a.a(K.f2104a, "MtuSize: " + mtuSize);
            return mtuSize;
        }

        @Override // b.InterfaceC0379m
        public void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr) {
            if (this.f2116a.getConnectState() == 2) {
                a.a.a.a.b.m.a.a(K.f2104a, String.format("Send data to node(%s) via proxy node(%s)", MeshParserUtils.bytesToHex(baseMeshNode.getUnicastAddress(), false), this.f2119d.getAddress()));
                this.f2116a.sendPdu(bArr);
                a.a.a.a.b.l.c.a(baseMeshNode.getUnicastAddressInt(), "0");
                return;
            }
            if (!MeshDeviceInfoManager.getInstance().isLowCostDeviceExist() || Build.VERSION.SDK_INT < 21) {
                return;
            }
            a.a.a.a.b.m.a.a(K.f2104a, String.format("Send data to node(%s) via adv channel", MeshParserUtils.bytesToHex(baseMeshNode.getUnicastAddress(), false)));
            if (FastProvisionManager.getInstance().getInProvisionProgress()) {
                a.a.a.a.b.m.a.b(K.f2104a, "Exist provision activity for tinyMesh, discard");
                return;
            }
            if (K.this.t == null) {
                K k = K.this;
                Context context = k.e;
                byte[] bArrD = K.this.s.d();
                K k2 = K.this;
                k.t = new a.a.a.a.b.k.d(context, bArrD, k2, k2.w);
            }
            K.this.t.a(baseMeshNode, bArr);
            a.a.a.a.b.l.c.a(baseMeshNode.getUnicastAddressInt(), "1");
        }

        public void a(List<SIGMeshBizRequest> list) {
            for (SIGMeshBizRequest sIGMeshBizRequest : list) {
                if (sIGMeshBizRequest.l().getOpcode() == 81) {
                    sIGMeshBizRequest.a(b());
                }
            }
            this.f2118c.a(list);
        }

        public final byte[] b() {
            if (this.f2119d == null || K.this.s == null) {
                return null;
            }
            return Arrays.copyOf(SecureUtils.calculateSha256(String.format("%s,%s,67656e69657368617265343536313233", this.f2119d.getAddress().replace(":", "").toLowerCase(), MeshParserUtils.bytesToHex(K.this.s.d(), false).toLowerCase()).getBytes()), 16);
        }

        public List<SIGMeshBizRequest> c() {
            return this.f2118c.b();
        }

        public boolean d() {
            return this.f2116a.isConnected() && this.f2116a.isReady();
        }

        public void a() {
            this.f2116a.close();
            this.f2116a.setNeedRequestMtu(true);
            f();
        }

        public d(BleMeshManager bleMeshManager, C0378l c0378l, BluetoothDevice bluetoothDevice) {
            this.f2116a = bleMeshManager;
            this.f2117b = c0378l;
            this.f2117b.a(this);
            this.f2117b.a(K.this.x);
            this.f2119d = bluetoothDevice;
            this.f2118c = new C0321g(this.f2117b);
        }

        public void a(long j) {
            this.e = j;
            this.f2118c.a(j);
        }

        public void a(int i) {
            this.f2118c.a(i);
        }

        public void a(BleMeshManager.WriteReadType writeReadType) {
            BleMeshManager bleMeshManager = this.f2116a;
            if (bleMeshManager == null || !bleMeshManager.isConnected()) {
                return;
            }
            this.f2116a.changeReadWriteType(writeReadType);
        }

        public void a(ProxyCommunicationQuality proxyCommunicationQuality) {
            this.f = proxyCommunicationQuality;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SubnetsBiz.java */
    interface e {
        void a(@NonNull d dVar);
    }

    public K(Context context, u.a aVar, q qVar) {
        this.f2107d = 3;
        this.G = 0;
        this.e = context;
        f2104a += (hashCode() % 1000000);
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(hashCode() % 1000000));
        sb.append(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        int i = this.G;
        this.G = i + 1;
        sb.append(i);
        String string = sb.toString();
        this.o = new Handler(Looper.getMainLooper());
        this.s = aVar;
        this.n = aVar.i();
        this.f2107d = 1;
        this.h = new C0378l(this.e);
        this.h.a(qVar);
        this.h.a(new A(this));
        this.x = qVar;
        this.u = new C0321g(this.h, string);
        this.g = this.n;
        BLEScannerProxy.getInstance().setOnMeshNetworkPUDListener(new B(this));
    }

    public boolean l() {
        return this.n && this.g && this.y.size() < this.f2107d;
    }

    public final void m() {
        if (this.F) {
            BleMeshManager.WriteReadType writeReadType = BleMeshManager.WriteReadType.WRITE_AND_READ;
            for (Map.Entry<String, d> entry : this.f.entrySet()) {
                BleMeshManager bleMeshManager = entry.getValue().f2116a;
                if (bleMeshManager != null && bleMeshManager.isConnected()) {
                    a.a.a.a.b.m.a.a(f2104a, String.format("read-write-splitting, make %s a %s node", entry.getKey(), writeReadType.toString()));
                    bleMeshManager.changeReadWriteType(writeReadType);
                    BleMeshManager.WriteReadType writeReadType2 = BleMeshManager.WriteReadType.WRITE_AND_READ;
                    writeReadType = writeReadType == writeReadType2 ? BleMeshManager.WriteReadType.WRITE : writeReadType2;
                }
            }
        }
    }

    public void n() {
        e();
    }

    public final void o() {
        b bVar;
        if (this.C.size() <= 0 || (bVar = this.D) == null) {
            return;
        }
        bVar.c();
    }

    @Override // aisble.BleManagerCallbacks
    public void onBatteryValueReceived(@NonNull BluetoothDevice bluetoothDevice, int i) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBonded(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingFailed(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingRequired(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataReceived(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        a.a.a.a.b.m.a.a(f2104a, "Received data from device: " + bluetoothDevice.getAddress());
        BaseMeshNode baseMeshNode = this.k;
        if (baseMeshNode == null) {
            a.a.a.a.b.m.a.d(f2104a, "The mesh node is null, discard the received data!!!");
            return;
        }
        d dVar = this.f.get(bluetoothDevice.getAddress());
        if (dVar == null) {
            a.a.a.a.b.m.a.d(f2104a, "Received message from unknown proxy interface, discard the received data!!!");
            return;
        }
        int realtimeRssiForProxyNode = dVar.f2116a.getRealtimeRssiForProxyNode();
        if (this.v == null) {
            this.v = new a.a.a.a.b.h.a();
        }
        this.v.a(realtimeRssiForProxyNode);
        dVar.f2117b.a(baseMeshNode, i, bArr, this.v);
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataSent(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnected(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.c(f2104a, String.format("Subnets(%s, %s) connected", this.p, bluetoothDevice.getAddress()));
        c(bluetoothDevice);
        m();
        a(bluetoothDevice, new G(this));
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnecting(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(f2104a, String.format("Subnets(%s, %s) connecting", this.p, bluetoothDevice.getAddress()));
        d();
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnected(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.c(f2104a, String.format("Subnets(%s, %s) disconnected", this.p, bluetoothDevice.getAddress()));
        c(bluetoothDevice);
        this.y.remove(bluetoothDevice.getAddress());
        Integer num = this.B.get(bluetoothDevice.getAddress());
        if (num != null) {
            f2105b.a(num.intValue());
        }
        a.a.a.a.b.m.a.a(f2104a, "pending connect and connected queue size: " + this.y.size());
        int i = this.l;
        d();
        m();
        a(bluetoothDevice, new H(this));
        if (this.n) {
            a(j());
        }
        int i2 = this.l;
        if (i2 == 0) {
            a(i, i2);
            k();
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnecting(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(f2104a, String.format("Subnets(%s, %s) disconnecting", this.p, bluetoothDevice.getAddress()));
        d();
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceNotSupported(@NonNull BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceReady(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.c(f2104a, String.format("Subnets(%s, %s) ready", this.p, bluetoothDevice.getAddress()));
        if (this.m == null) {
            this.m = bluetoothDevice;
        }
        int i = this.l;
        Integer num = this.B.get(bluetoothDevice.getAddress());
        if (num != null) {
            f2105b.a(num.intValue());
        }
        this.l = 2;
        c();
        a(i, this.l);
        c cVar = this.r;
        if (cVar != null) {
            cVar.onMeshChannelReady(this);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onError(@NonNull BluetoothDevice bluetoothDevice, String str, int i) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback
    public void onFastProvisionDataSend(BaseMeshNode baseMeshNode, byte[] bArr) {
        a.a.a.a.b.i.J j;
        if (Build.VERSION.SDK_INT >= 21 && (j = this.w) != null && j.e()) {
            a.a.a.a.b.m.a.b(f2104a, "Exist provision activity for tinyMesh, discard");
            return;
        }
        a.a.a.a.b.m.a.c(f2104a, "onFastProvisionDataSend: " + ConvertUtils.bytes2HexString(bArr));
        this.h.a(baseMeshNode, 18, bArr);
    }

    @Override // aisble.BleManagerCallbacks
    public void onLinkLossOccurred(@NonNull BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.d(f2104a, String.format("Subnets(%s, %s) loss link", this.p, bluetoothDevice.getAddress()));
        c(bluetoothDevice);
        this.y.remove(bluetoothDevice.getAddress());
        int i = this.l;
        d();
        m();
        a(bluetoothDevice, new I(this));
        if (this.n) {
            a(j());
        }
        if (this.l == 0) {
            this.i = false;
            if (this.n && i == 2) {
                a(false, UtError.MESH_LINK_LOSS_OCCURRED.getMsg());
                a(a(R.string.state_linkloss_occur));
            }
            k();
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback
    public void onReceiveFastProvisionData(BaseMeshNode baseMeshNode, byte[] bArr) {
        a.a.a.a.b.i.J j;
        if (Build.VERSION.SDK_INT >= 21 && (j = this.w) != null && j.e()) {
            a.a.a.a.b.m.a.b(f2104a, "Exist provision activity for tinyMesh, discard");
        } else {
            this.i = false;
            this.h.a(baseMeshNode, 18, bArr, (a.a.a.a.b.h.a) null);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onServicesDiscovered(@NonNull BluetoothDevice bluetoothDevice, boolean z) {
        a.a.a.a.b.m.a.a(f2104a, "onServicesDiscovered...");
        a(a(R.string.state_initializing));
        if (this.i) {
            this.i = false;
        } else if (this.j && this.n) {
            a(bluetoothDevice);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public boolean shouldEnableBatteryLevelNotifications(@NonNull BluetoothDevice bluetoothDevice) {
        return false;
    }

    public final void c(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return;
        }
        Iterator<ExtendedBluetoothDevice> it = this.z.iterator();
        while (it.hasNext()) {
            if (bluetoothDevice.getAddress().equalsIgnoreCase(it.next().getAddress())) {
                it.remove();
                return;
            }
        }
    }

    public final void d() {
        int i;
        if (this.f.size() == 0) {
            this.l = 0;
            return;
        }
        int i2 = this.l;
        int size = this.f.size();
        SparseIntArray sparseIntArray = new SparseIntArray();
        Iterator<Map.Entry<String, d>> it = this.f.entrySet().iterator();
        while (it.hasNext()) {
            BleMeshManager bleMeshManager = it.next().getValue().f2116a;
            sparseIntArray.put(bleMeshManager.getConnectState(), sparseIntArray.get(bleMeshManager.getConnectState(), 0) + 1);
        }
        if (sparseIntArray.get(2) > 0) {
            if (this.l != 2) {
                o();
            }
            this.l = 2;
            this.g = this.y.size() < this.f2107d;
            a.a.a.a.b.m.a.c(f2104a, "Current connection state change to STATE_CONNECTED, mIsMultiProxyAcceptable: " + this.g);
        } else if (sparseIntArray.get(0) == size) {
            this.l = 0;
            this.g = true;
            a.a.a.a.b.m.a.d(f2104a, "Current connection state change to STATE_DISCONNECTED");
        } else if (sparseIntArray.get(1) > 0) {
            this.l = 1;
        } else {
            this.l = 3;
        }
        c cVar = this.r;
        if (cVar == null || i2 == (i = this.l)) {
            return;
        }
        cVar.onConnectionStateChanged(this, i2, i);
    }

    public final void e() {
        a.a.a.a.b.m.a.a(f2104a, "Enqueue disconnect task");
        if (this.f.size() == 0) {
            return;
        }
        Iterator<Map.Entry<String, d>> it = this.f.entrySet().iterator();
        while (it.hasNext()) {
            BleMeshManager bleMeshManager = it.next().getValue().f2116a;
            if (bleMeshManager.getConnectState() == 2 || bleMeshManager.getConnectState() == 1) {
                bleMeshManager.disconnect().enqueue();
            }
        }
    }

    public int f() {
        return this.l;
    }

    public C0378l g() {
        for (d dVar : this.A) {
            if (dVar.d()) {
                return dVar.f2117b;
            }
        }
        return this.h;
    }

    public BluetoothDevice h() {
        return this.m;
    }

    public List<BluetoothDevice> i() {
        LinkedList linkedList = new LinkedList();
        for (d dVar : this.A) {
            if (dVar.d()) {
                linkedList.add(dVar.f2119d);
            }
        }
        return linkedList;
    }

    public u.a j() {
        return this.s;
    }

    public final void k() {
        MeshService.OnDisconnectListener onDisconnectListener = this.q;
        if (onDisconnectListener != null) {
            onDisconnectListener.onDisconnected();
            this.q = null;
        }
    }

    public void b(String str) {
        this.p = str;
    }

    public void b(ExtendedBluetoothDevice extendedBluetoothDevice, boolean z) {
        if (extendedBluetoothDevice != null && this.y.size() < this.f2107d) {
            if (b(extendedBluetoothDevice)) {
                a.a.a.a.b.m.a.d(f2104a, "Connect, already connected or pending connect to the device: " + extendedBluetoothDevice.getAddress() + ", do nothing");
                return;
            }
            this.y.add(extendedBluetoothDevice.getAddress());
            boolean z2 = this.z.size() == 0;
            this.z.add(extendedBluetoothDevice);
            if (this.n) {
                if (z2) {
                    Handler handler = this.o;
                    C c2 = new C(this);
                    this.H = c2;
                    handler.postDelayed(c2, 3000L);
                }
                if (this.y.size() >= this.f2107d) {
                    this.o.removeCallbacks(this.H);
                    b();
                    Iterator<ExtendedBluetoothDevice> it = this.z.iterator();
                    while (it.hasNext()) {
                        a(it.next());
                    }
                    this.z.clear();
                    return;
                }
                return;
            }
            a(extendedBluetoothDevice);
            return;
        }
        a.a.a.a.b.m.a.b(f2104a, "Connect, device is null or exceeded pending queue limit");
    }

    public final void c(ExtendedBluetoothDevice extendedBluetoothDevice) {
        BleMeshManager bleMeshManagerA = a(extendedBluetoothDevice, true);
        if (bleMeshManagerA == null) {
            a.a.a.a.b.m.a.b(f2104a, "Connect, error when allocate bleMeshManager");
            return;
        }
        if (extendedBluetoothDevice.getScanRecord() != null) {
            UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(extendedBluetoothDevice.getScanRecord().getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)));
            a.a.a.a.b.m.a.c(f2104a, "unprovisionedMeshNodeData.isFastProvisionMesh ? " + unprovisionedMeshNodeData.isFastProvisionMesh());
            if (unprovisionedMeshNodeData.isFastProvisionMesh()) {
                return;
            }
            bleMeshManagerA.connect(extendedBluetoothDevice.getDevice()).enqueue();
            this.j = true;
            return;
        }
        a.a.a.a.b.m.a.c(f2104a, "mScannerRecord is null");
        bleMeshManagerA.connect(extendedBluetoothDevice.getDevice()).retry(10, ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED).enqueue();
        this.j = true;
    }

    public void a(BaseMeshNode baseMeshNode) {
        this.k = baseMeshNode;
    }

    public boolean a(BleMeshManager bleMeshManager, ProvisionedMeshNode provisionedMeshNode, a.a.a.a.b.i.J j) {
        d dVar;
        int i = this.l;
        if ((i == 2 || i == 1) && !l()) {
            a.a.a.a.b.m.a.d(f2104a, "Current connection state is connected or connecting: " + this.l);
            return false;
        }
        if (bleMeshManager == null || bleMeshManager.getBluetoothDevice() == null || bleMeshManager.getConnectState() == 0) {
            return false;
        }
        a.a.a.a.b.m.a.c(f2104a, String.format("%s attach connection status", this.p));
        this.y.add(bleMeshManager.getBluetoothDevice().getAddress());
        if (this.f.size() == 0) {
            dVar = new d(bleMeshManager, this.h, bleMeshManager.getBluetoothDevice());
        } else {
            dVar = new d(bleMeshManager, bleMeshManager.getBluetoothDevice());
        }
        this.f.put(bleMeshManager.getBluetoothDevice().getAddress(), dVar);
        this.A.add(dVar);
        this.w = j;
        bleMeshManager.setGattCallbacks(this);
        bleMeshManager.setProvisioningComplete(true);
        int i2 = this.l;
        this.m = bleMeshManager.getBluetoothDevice();
        this.l = bleMeshManager.getConnectState();
        this.k = provisionedMeshNode;
        d();
        a(i2, this.l);
        return true;
    }

    public final void c() {
        Iterator<d> it = this.A.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().d()) {
                i++;
            }
        }
        long j = 0;
        for (d dVar : this.A) {
            if (dVar.d()) {
                dVar.a(j);
                j += (long) f2106c;
                dVar.a(i);
            }
        }
    }

    public final void b() {
        if (Build.VERSION.SDK_INT >= 21) {
            byte[] bArrA = SIGMeshBizRequestGenerator.a((byte) 8, this.y, this.s.c(), 0);
            a.a.a.a.a.g.c().d();
            a.a.a.a.a.g.c().a(bArrA, 500, new F(this));
        }
    }

    public void b(BluetoothDevice bluetoothDevice) {
        int i = this.l;
        if (i == 2 || i == 1) {
            a.a.a.a.b.m.a.c(f2104a, "No need to connect again");
            return;
        }
        if (bluetoothDevice == null) {
            a.a.a.a.b.m.a.b(f2104a, "device is null");
            return;
        }
        a.a.a.a.b.m.a.a(f2104a, "connect to specified device: " + bluetoothDevice.getAddress());
        b(new ExtendedBluetoothDevice(new ScanResult(bluetoothDevice, ScanRecord.parseFromBytes(new byte[0]), -50, 0L)), true);
    }

    public void a(c cVar) {
        this.r = cVar;
    }

    public void a(Map<ProxyCommunicationQuality, List<ExtendedBluetoothDevice>> map) {
        if (map != null && this.y.size() <= this.f2107d) {
            byte[] bArr = {0, 0, 0};
            for (d dVar : this.A) {
                if (dVar != null && dVar.f2116a.isConnected() && dVar.f != null && dVar.f.getLevel() != 0) {
                    int level = dVar.f.getLevel() - 1;
                    bArr[level] = (byte) (bArr[level] + 1);
                }
            }
            int i = (this.f2107d + 1) / 2;
            for (int length = bArr.length - 1; length >= 0; length--) {
                int i2 = i - bArr[length];
                if (i2 < 0) {
                    i2 = 0;
                }
                int i3 = length + 1;
                List<ExtendedBluetoothDevice> list = map.get(ProxyCommunicationQuality.getQualityViaLevel(i3));
                if (list != null && list.size() > 0) {
                    int i4 = i2;
                    for (int i5 = 0; i5 < i4 && i5 < list.size(); i5++) {
                        if (!this.y.contains(list.get(i5).getAddress())) {
                            a.a.a.a.b.m.a.a("multi_proxy_selector", String.format(Locale.getDefault(), "Connect %s, quality: %s, Rssi: %d", list.get(i5).getAddress(), ProxyCommunicationQuality.getQualityViaLevel(i3), Integer.valueOf(list.get(i5).getRssi())));
                            b(list.get(i5), true);
                            i4--;
                        }
                    }
                    i2 = i4;
                }
                i = i2 + 1;
            }
            return;
        }
        a.a.a.a.b.m.a.b(f2104a, "Connect, device is null or exceeded pending queue limit");
    }

    public final boolean b(ExtendedBluetoothDevice extendedBluetoothDevice) {
        if (extendedBluetoothDevice == null || TextUtils.isEmpty(extendedBluetoothDevice.getAddress())) {
            return false;
        }
        return this.y.contains(extendedBluetoothDevice.getAddress());
    }

    public void b(int i) {
        this.f2107d = i;
    }

    public final void a(ExtendedBluetoothDevice extendedBluetoothDevice) {
        this.B.put(extendedBluetoothDevice.getAddress(), Integer.valueOf(f2105b.a(extendedBluetoothDevice, new D(this, extendedBluetoothDevice), new E(this, extendedBluetoothDevice))));
    }

    public void a(MeshService.OnDisconnectListener onDisconnectListener) {
        d();
        a.a.a.a.b.m.a.a(f2104a, "Prepare disconnect, current connection state:" + this.l);
        this.q = onDisconnectListener;
        if (this.l == 0) {
            this.q.onDisconnected();
            this.q = null;
        } else {
            e();
        }
    }

    public void a(List<SIGMeshBizRequest> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        ArrayList<Pair> arrayList = new ArrayList();
        int size = 0;
        int i = 0;
        for (Map.Entry<String, d> entry : this.f.entrySet()) {
            if (entry.getValue().d()) {
                i++;
                arrayList.add(new Pair(entry.getValue(), new ArrayList()));
            }
        }
        if (i > 0) {
            LinkedList linkedList = new LinkedList();
            if (this.E) {
                synchronized (this.C) {
                    this.C.addAll(list);
                }
                synchronized (this) {
                    if (this.D == null) {
                        this.D = new b();
                        this.D.b(100);
                    }
                }
                this.D.c();
                return;
            }
            for (SIGMeshBizRequest sIGMeshBizRequest : list) {
                Pair pair = (Pair) arrayList.get(size);
                ((ArrayList) pair.second).add(sIGMeshBizRequest);
                sIGMeshBizRequest.a(((d) pair.first).f2117b);
                linkedList.add(sIGMeshBizRequest);
                size = (size + 1) % arrayList.size();
            }
            for (Pair pair2 : arrayList) {
                ((d) pair2.first).a((List<SIGMeshBizRequest>) pair2.second);
            }
            return;
        }
        if (this.s.f()) {
            this.u.a(list);
            return;
        }
        Iterator<SIGMeshBizRequest> it = list.iterator();
        while (it.hasNext()) {
            Utils.notifyFailed(it.next().m(), -23, "Unreachable");
        }
    }

    public void a(boolean z) {
        this.u.a(z);
    }

    public final String a(int i) {
        return this.e.getString(i);
    }

    public final void a(BluetoothDevice bluetoothDevice) {
        if (this.k == null || bluetoothDevice == null) {
            return;
        }
        d dVar = this.f.get(bluetoothDevice.getAddress());
        if (dVar != null) {
            C0378l c0378l = dVar.f2117b;
            ProvisionedMeshNode provisionedMeshNode = (ProvisionedMeshNode) this.k;
            c0378l.a(provisionedMeshNode, 0, new byte[]{0});
            this.o.postDelayed(new z(this, c0378l, provisionedMeshNode), 500L);
            return;
        }
        a.a.a.a.b.m.a.b(f2104a, "Internal error");
    }

    public final void a(boolean z, String str) {
        Intent intent = new Intent(Utils.ACTION_IS_CONNECTED);
        intent.putExtra(Utils.EXTRA_DATA, z);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra(Utils.EXTRA_CONNECT_FAIL_MSG, str);
        }
        LocalBroadcastManager.getInstance(this.e).sendBroadcast(intent);
    }

    public final void a(String str) {
        Intent intent = new Intent(Utils.ACTION_CONNECTION_STATE);
        intent.putExtra(Utils.EXTRA_DATA, str);
        LocalBroadcastManager.getInstance(this.e).sendBroadcast(intent);
    }

    public final void a(int i, int i2) {
        if ((i2 == 2 || i2 == 0) && this.j && this.n) {
            if ((i == 1 && i2 == 2) || ((i == 2 && i2 == 0) || ((i == 3 && i2 == 0) || (i == 0 && i2 == 2)))) {
                a(i2 == 2, (String) null);
                if (i2 == 2) {
                    a(a(R.string.state_disconnected));
                }
            }
        }
    }

    public final BleMeshManager a(ExtendedBluetoothDevice extendedBluetoothDevice, boolean z) {
        d dVar;
        if (extendedBluetoothDevice == null || extendedBluetoothDevice.getDevice() == null) {
            return null;
        }
        BluetoothDevice device = extendedBluetoothDevice.getDevice();
        String address = device.getAddress();
        d dVar2 = this.f.get(address);
        ProxyCommunicationQuality qualityViaRssi = ProxyCommunicationQuality.getQualityViaRssi(extendedBluetoothDevice.getRssi());
        a.a.a.a.b.m.a.c(f2104a, "allocate mesh channel, communication quality: " + qualityViaRssi.getLevel());
        if (dVar2 != null && !z) {
            dVar2.a(qualityViaRssi);
            return dVar2.f2116a;
        }
        Context context = this.e;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(hashCode() % 1000000));
        sb.append(OpenAccountUIConstants.UNDER_LINE);
        int i = this.G;
        this.G = i + 1;
        sb.append(i);
        BleMeshManager bleMeshManager = new BleMeshManager(context, sb.toString());
        bleMeshManager.setGattCallbacks(this);
        bleMeshManager.setProvisioningComplete(true);
        if (this.f.size() == 0) {
            dVar = new d(bleMeshManager, this.h, device);
        } else {
            dVar = new d(bleMeshManager, device);
        }
        dVar.a(qualityViaRssi);
        this.A.add(dVar);
        this.f.put(address, dVar);
        return bleMeshManager;
    }

    public final void a(d dVar) {
        List<SIGMeshBizRequest> listC = dVar.c();
        if (listC == null || listC.size() == 0) {
            return;
        }
        a(listC);
    }

    public final void a(BluetoothDevice bluetoothDevice, e eVar) {
        d dVar;
        if (bluetoothDevice == null || eVar == null || (dVar = this.f.get(bluetoothDevice.getAddress())) == null) {
            return;
        }
        eVar.a(dVar);
    }

    public final void a(u.a aVar) {
        a.a.a.a.b.m.a.c(f2104a, "Trigger re-connect " + aVar);
        a.a.a.a.b.G.a().d().a(aVar, false);
    }
}

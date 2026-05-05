package b;

import aisble.BleManager;
import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanCallback;
import aisscanner.ScanFilter;
import aisscanner.ScanResult;
import aisscanner.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import b.K;
import b.u;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import meshprovisioner.ProxyCommunicationQuality;

/* JADX INFO: compiled from: SIGMeshNetworkTransportManager.java */
/* JADX INFO: loaded from: classes.dex */
public class y implements K.c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f2223a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public u f2226d;
    public q e;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public volatile boolean f2225c = false;
    public List<String> f = new ArrayList();
    public List<K.c> i = new ArrayList();
    public Map<String, ScanResult> j = new LinkedHashMap();
    public long k = -1;
    public volatile boolean l = false;
    public final Runnable m = new v(this);
    public final ScanCallback n = new w(this);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Handler f2224b = new Handler(Looper.getMainLooper());
    public Queue<K> g = new LinkedList();
    public Queue<a> h = new ConcurrentLinkedQueue();

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: SIGMeshNetworkTransportManager.java */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public u.a f2227a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public boolean f2228b = false;

        public a(u.a aVar) {
            this.f2227a = aVar;
        }

        public void b() {
            this.f2228b = true;
        }

        public boolean a() {
            return this.f2228b;
        }
    }

    public y(Context context, u uVar, q qVar) {
        this.f2223a = context;
        this.f2226d = uVar;
        this.e = qVar;
    }

    @Override // b.K.c
    public void onConnectionStateChanged(K k, int i, int i2) {
        if (i2 != 0) {
            if (i2 == 2) {
                if (!this.g.contains(k)) {
                    this.g.add(k);
                }
                Iterator<a> it = this.h.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    a next = it.next();
                    if (next.f2227a != null && next.f2227a.equals(k.j())) {
                        next.b();
                        break;
                    }
                }
                if (this.h.size() > 0 && !a()) {
                    a.a.a.a.b.m.a.c("SIGMeshNetworkTransportManager", "All waiting connection task finished");
                    e();
                    this.h.clear();
                }
                for (K.c cVar : this.i) {
                    if (cVar != null) {
                        cVar.onConnectionStateChanged(k, i, i2);
                    }
                }
                return;
            }
            if (i2 != 3) {
                return;
            }
        }
        this.g.remove(k);
        if (k.l()) {
            a(k.j(), false);
        }
    }

    @Override // b.K.c
    public void onMeshChannelReady(K k) {
        for (K.c cVar : this.i) {
            if (cVar != null) {
                cVar.onMeshChannelReady(k);
            }
        }
    }

    public boolean b(byte[] bArr) {
        return bArr != null && bArr[0] == 0;
    }

    public final void c() {
        this.f2224b.removeCallbacks(this.m);
        this.f2224b.postDelayed(this.m, BleManager.CONNECTION_TIMEOUT_THRESHOLD);
    }

    public void d() {
        if (!Utils.isBleEnabled()) {
            a.a.a.a.b.m.a.b("SIGMeshNetworkTransportManager", "Invalid scan: Bluetooth adapter is disabled");
            return;
        }
        if (this.f2225c) {
            return;
        }
        a.a.a.a.b.m.a.a("SIGMeshNetworkTransportManager", "startScan...");
        this.f2225c = true;
        this.k = System.currentTimeMillis();
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(1).setReportDelay(0L).setUseHardwareFilteringIfSupported(true).build();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(BleMeshManager.MESH_PROXY_UUID)).build());
        if (Utils.isBleEnabled()) {
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            try {
                if (Build.VERSION.SDK_INT < 31 || Utils.checkBlePermission(this.f2223a)) {
                    scanner.startScan(arrayList, scanSettingsBuild, this.n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.f2224b.postDelayed(this.m, BleManager.CONNECTION_TIMEOUT_THRESHOLD);
        }
    }

    public final void e() {
        a.a.a.a.b.m.a.a("SIGMeshNetworkTransportManager", "stopScan...");
        this.f2224b.removeCallbacks(this.m);
        if (Utils.isBleEnabled()) {
            BluetoothLeScannerCompat.getScanner().stopScan(this.n);
        }
        this.f2225c = false;
        this.k = -1L;
        this.j.clear();
    }

    public void f() {
        if (this.l) {
            a.a.a.a.b.m.a.d("SIGMeshNetworkTransportManager", "tryConnectAllSubnets: connection is not currently allowed");
            return;
        }
        u.a aVarD = a.a.a.a.b.G.a().d().d();
        if (aVarD != null && aVarD.e() == null) {
            aVarD.a(new K(this.f2223a, aVarD, this.e));
        }
        d();
    }

    public void g() {
        a.a.a.a.b.m.a.a("SIGMeshNetworkTransportManager", "Try stop connect activity");
        if (this.f2225c) {
            e();
        }
    }

    public void h() {
        a.a.a.a.b.m.a.c("SIGMeshNetworkTransportManager", "unlock connection");
        this.l = false;
    }

    public void b(K.c cVar) {
        this.i.remove(cVar);
    }

    public void b() {
        a.a.a.a.b.m.a.c("SIGMeshNetworkTransportManager", "lock connection");
        this.l = true;
    }

    public void a(u.a aVar, boolean z) {
        if (this.l) {
            a.a.a.a.b.m.a.d("SIGMeshNetworkTransportManager", "tryConnectSpecifiedSubnets: connection is not currently allowed");
            return;
        }
        a.a.a.a.b.m.a.a("SIGMeshNetworkTransportManager", "Try connect target " + aVar);
        if (aVar.f() && !aVar.h()) {
            a.a.a.a.b.m.a.a("SIGMeshNetworkTransportManager", String.format("%s is available, do nothing", aVar));
            return;
        }
        if (this.h.size() > 2) {
            this.h.remove();
        }
        if (aVar.e() == null && aVar.i()) {
            aVar.a(new K(this.f2223a, aVar, this.e));
        }
        a(aVar);
        if (this.f2225c) {
            a.a.a.a.b.m.a.d("SIGMeshNetworkTransportManager", "Currently scanning, do nothing");
            if (z) {
                c();
                return;
            }
            return;
        }
        d();
    }

    public void b(u.a aVar) {
        for (a aVar2 : this.h) {
            if (aVar2 != null && aVar2.f2227a.equals(aVar)) {
                this.h.remove(aVar2);
                return;
            }
        }
    }

    public boolean a(byte[] bArr) {
        return bArr != null && bArr[0] == 1;
    }

    public void a(K.c cVar) {
        if (cVar != null) {
            this.i.add(cVar);
        }
    }

    public final void a(u.a aVar) {
        for (a aVar2 : this.h) {
            if (aVar2 != null && aVar2.f2227a.equals(aVar)) {
                return;
            }
        }
        this.h.add(new a(aVar));
    }

    public final boolean a(String str) {
        return this.k != -1 && System.currentTimeMillis() - this.k > 3000 && MeshDeviceInfoManager.getInstance().isLowPowerDeviceViaMac(str);
    }

    public final boolean a() {
        for (a aVar : this.h) {
            if (!aVar.a() || aVar.f2227a.h()) {
                return true;
            }
        }
        return false;
    }

    public final void a(ScanResult scanResult, K k) {
        if (scanResult == null) {
            return;
        }
        this.j.put(scanResult.getDevice().getAddress(), scanResult);
        a.a.a.a.b.m.a.a("multi_proxy_selector", "Cache device: " + scanResult.getDevice().getAddress() + ", Rssi: " + scanResult.getRssi());
        if (this.k == -1 || System.currentTimeMillis() - this.k <= 3000 || k == null) {
            return;
        }
        a.a.a.a.b.m.a.a("multi_proxy_selector", "Order proxy devices via quality level");
        this.k = System.currentTimeMillis();
        ArrayList<ScanResult> arrayList = new ArrayList(this.j.values());
        Collections.sort(arrayList, new x(this));
        HashMap map = new HashMap();
        for (ScanResult scanResult2 : arrayList) {
            ProxyCommunicationQuality qualityViaRssi = ProxyCommunicationQuality.getQualityViaRssi(scanResult2.getRssi());
            List<ExtendedBluetoothDevice> linkedList = map.get(qualityViaRssi);
            if (linkedList == null) {
                linkedList = new LinkedList<>();
                map.put(qualityViaRssi, linkedList);
            }
            linkedList.add(new ExtendedBluetoothDevice(scanResult2));
        }
        k.a(map);
    }
}

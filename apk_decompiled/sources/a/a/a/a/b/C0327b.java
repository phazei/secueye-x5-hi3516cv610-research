package a.a.a.a.b;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: renamed from: a.a.a.a.b.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AdjustConnectableAdvertiseIntervalSlidingWindowManager.java */
/* JADX INFO: loaded from: classes.dex */
public class C0327b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static C0327b f1289a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static AtomicBoolean f1290b = new AtomicBoolean(false);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public List<String> f1291c = new LinkedList();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public List<String> f1292d = new LinkedList();
    public List<String> e = new LinkedList();
    public int f = 4;
    public boolean g;
    public boolean h;
    public int i;
    public Runnable j;
    public Handler k;
    public List<BleMeshManager> l;

    public C0327b() {
        this.g = a.a.a.a.b.d.a.f1316b || a.a.a.a.b.d.a.f1315a;
        this.h = false;
        this.l = new LinkedList();
        this.k = new Handler(Looper.getMainLooper());
    }

    public static C0327b b() {
        if (f1289a == null) {
            synchronized (a.a.a.a.a.g.class) {
                if (f1289a == null) {
                    f1289a = new C0327b();
                }
            }
        }
        return f1289a;
    }

    public void a(List<String> list, int i) {
        this.f1292d = list;
        this.i = i;
        a();
    }

    public void a(List<String> list) {
        this.f1291c = list;
    }

    public synchronized void a(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return;
        }
        String address = bluetoothDevice.getAddress();
        a.a.a.a.b.m.a.b("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "onPrepareConnectRemoteDevice: " + address);
        if (this.f1292d.contains(address)) {
            a.a.a.a.b.m.a.d("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "Already in sliding window, do nothing");
            return;
        }
        if (this.f1292d.size() < this.f) {
            this.f1292d.add(address);
            if (this.f1292d.size() < this.f) {
                for (int i = 0; i < this.f1291c.size() && this.f1292d.size() < this.f; i++) {
                    if (!this.f1292d.contains(this.f1291c.get(i))) {
                        this.f1292d.add(this.f1291c.get(i));
                    }
                }
            }
            a();
        } else {
            this.e.add(address);
        }
    }

    public void b(BleMeshManager bleMeshManager) {
        a.a.a.a.b.m.a.a("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "removeWriteableChannel: " + bleMeshManager);
        this.l.remove(bleMeshManager);
    }

    public synchronized void a(BluetoothDevice bluetoothDevice, boolean z) {
        boolean z2;
        if (bluetoothDevice == null) {
            return;
        }
        a.a.a.a.b.m.a.b("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "onConnectFinished: " + bluetoothDevice.getAddress());
        List<String> list = this.f1291c;
        if (list != null) {
            ListIterator<String> listIterator = list.listIterator();
            while (true) {
                if (listIterator.hasNext()) {
                    if (listIterator.next().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                        listIterator.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        List<String> list2 = this.f1292d;
        if (list2 != null) {
            ListIterator<String> listIterator2 = list2.listIterator();
            while (true) {
                if (listIterator2.hasNext()) {
                    if (listIterator2.next().equalsIgnoreCase(bluetoothDevice.getAddress())) {
                        listIterator2.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
            List<String> list3 = this.e;
            if (list3 == null || list3.size() <= 0) {
                z2 = false;
            } else {
                z2 = false;
                while (this.e.size() > 0 && this.f1292d.size() < this.f) {
                    this.f1292d.add(this.e.get(0));
                    this.e.remove(0);
                    z2 = true;
                }
            }
            if (z2) {
                if (this.f1292d.size() < this.f) {
                    for (int i = 0; i < this.f1291c.size() && this.f1292d.size() < this.f; i++) {
                        if (!this.f1292d.contains(this.f1291c.get(i))) {
                            this.f1292d.add(this.f1291c.get(i));
                        }
                    }
                }
                a();
            }
        }
    }

    public void a(BleMeshManager bleMeshManager) {
        a.a.a.a.b.m.a.a("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "addWriteableChannel: " + bleMeshManager);
        this.l.add(bleMeshManager);
    }

    public final void a() {
        List<String> list;
        if (this.g && Build.VERSION.SDK_INT >= 21 && (list = this.f1292d) != null && list.size() > 0) {
            b.u uVarD = G.a().d();
            boolean z = false;
            byte[] bArrA = SIGMeshBizRequestGenerator.a((byte) 11, this.f1292d, (uVarD == null || uVarD.d() == null) ? (byte) 0 : uVarD.d().c(), this.i);
            Runnable runnable = this.j;
            if (runnable != null) {
                this.k.removeCallbacks(runnable);
            }
            if (this.h) {
                boolean z2 = false;
                for (BleMeshManager bleMeshManager : this.l) {
                    if (bleMeshManager != null && bleMeshManager.isConnected()) {
                        a.a.a.a.b.m.a.d("AdjustConnectableAdvertiseIntervalSlidingWindowManager", "Use gatt channel to adjust adv internal, bleMeshManager: " + bleMeshManager);
                        byte[] bArr = new byte[bArrA.length + 2];
                        bArr[0] = 1;
                        bArr[1] = -88;
                        System.arraycopy(bArrA, 0, bArr, 2, bArrA.length);
                        bleMeshManager.sendPdu(bArr);
                        z2 = true;
                    }
                }
                z = z2;
            }
            if (z) {
                return;
            }
            f1290b.set(true);
            a.a.a.a.a.g.c().a(bArrA, 500, new C0314a(this));
        }
    }
}

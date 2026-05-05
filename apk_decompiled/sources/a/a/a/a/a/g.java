package a.a.a.a.a;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
@RequiresApi(api = 21)
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static volatile g f1182a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public BluetoothLeAdvertiser f1183b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public BluetoothManager f1184c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BluetoothAdapter f1185d;
    public AdvertiseCallback j;
    public Context m;
    public int g = 0;
    public volatile boolean l = false;
    public Handler e = new Handler(Looper.getMainLooper());
    public Runnable f = new a.a.a.a.a.a(this);
    public a h = new a(null, null);
    public b i = new b(null, null);
    public LinkedBlockingDeque<a.a.a.a.a.a.a.b.a> k = new LinkedBlockingDeque<>();

    /* JADX INFO: compiled from: AdvertiseManager.java */
    class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public a.a.a.a.a.a.a.b.a f1186a;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public List<byte[]> f1188c;
        public BleAdvertiseCallback<Boolean> e;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public boolean f1187b = false;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f1189d = -1;
        public int f = 0;

        public a(List<byte[]> list, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
            this.f1188c = list;
            this.e = bleAdvertiseCallback;
        }

        public static /* synthetic */ int c(a aVar) {
            int i = aVar.f;
            aVar.f = i + 1;
            return i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f1188c != null) {
                int iA = a();
                if (!this.f1187b) {
                    a.a.a.a.b.m.a.b("AdvertiseManager", "AlternateAdvertiseTask not allowed " + ((int) this.f1186a.c()));
                    return;
                }
                a.a.a.a.b.m.a.c("AdvertiseManager", "running AlternateAdvertiseTask " + ((int) this.f1186a.c()) + ", index " + iA);
                byte[] bArr = this.f1188c.get(iA);
                g.this.e.removeCallbacks(g.this.i);
                g.this.i.a(bArr);
                g.this.i.a(new f(this));
                g.this.e.post(g.this.i);
            }
        }

        public final int a() {
            int i = this.f1189d + 1;
            this.f1189d = i;
            this.f1189d = i % this.f1188c.size();
            return this.f1189d;
        }

        @RequiresApi(api = 21)
        public final void b() {
            StringBuilder sb = new StringBuilder();
            sb.append("reset msg ");
            a.a.a.a.a.a.a.b.a aVar = this.f1186a;
            sb.append(aVar == null ? TmpConstant.GROUP_ROLE_UNKNOWN : Byte.valueOf(aVar.c()));
            a.a.a.a.b.m.a.c("AdvertiseManager", sb.toString());
            this.f = 0;
            this.f1189d = -1;
            g.this.e.removeCallbacks(this);
            g.this.e.removeCallbacks(g.this.i);
            this.f1187b = false;
            g.this.i.a();
        }

        public void a(a.a.a.a.a.a.a.b.a aVar) {
            this.f1186a = aVar;
            this.f1188c = aVar.a();
        }

        public void a(BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
            this.e = bleAdvertiseCallback;
        }

        public void a(boolean z) {
            this.f1187b = z;
        }
    }

    /* JADX INFO: compiled from: AdvertiseManager.java */
    class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public byte[] f1190a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public BleAdvertiseCallback<Boolean> f1191b;

        public b(byte[] bArr, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
            this.f1190a = bArr;
            this.f1191b = bleAdvertiseCallback;
        }

        @Override // java.lang.Runnable
        @RequiresApi(api = 21)
        public void run() {
            g.this.a(this.f1190a, this.f1191b);
        }

        public void a(byte[] bArr) {
            this.f1190a = bArr;
        }

        public void a(BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
            this.f1191b = bleAdvertiseCallback;
        }

        @RequiresApi(api = 21)
        public final void a() {
            a.a.a.a.b.m.a.c("AdvertiseManager", "reset");
            g.this.d();
        }
    }

    public static g c() {
        if (f1182a == null) {
            synchronized (g.class) {
                if (f1182a == null) {
                    f1182a = new g();
                }
            }
        }
        return f1182a;
    }

    public void b(Context context) {
        this.m = context;
    }

    @RequiresApi(api = 21)
    public void d() {
        a.a.a.a.b.m.a.c("AdvertiseManager", "stopAdvertise");
        if (this.f1183b == null || this.j == null || !Utils.isBleEnabled()) {
            return;
        }
        try {
            this.f1183b.stopAdvertising(this.j);
        } catch (Exception unused) {
        }
    }

    public void b(byte[] bArr, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
        this.e.post(new a.a.a.a.a.b(this, bArr, bleAdvertiseCallback));
    }

    @RequiresApi(api = 21)
    public void a(Context context) {
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "ble not supported");
            return;
        }
        this.f1184c = (BluetoothManager) context.getSystemService("bluetooth");
        BluetoothManager bluetoothManager = this.f1184c;
        if (bluetoothManager == null) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "failed to get bluetoothManager");
            return;
        }
        this.f1185d = bluetoothManager.getAdapter();
        BluetoothAdapter bluetoothAdapter = this.f1185d;
        if (bluetoothAdapter == null) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "failed to get bluetoothAdapter");
            return;
        }
        try {
            this.f1183b = bluetoothAdapter.getBluetoothLeAdvertiser();
            if (this.f1183b == null) {
                a.a.a.a.b.m.a.b("AdvertiseManager", "the device not support peripheral");
            }
        } catch (NullPointerException unused) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "failed Null pointer exists in room");
        }
    }

    public synchronized byte b() {
        int i = this.g;
        if (i >= 255) {
            this.g = 0;
            return (byte) this.g;
        }
        this.g = i + 1;
        return (byte) i;
    }

    @RequiresApi(api = 21)
    public AdvertiseSettings a(boolean z, int i) {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(2);
        builder.setConnectable(z);
        builder.setTimeout(i);
        builder.setTxPowerLevel(3);
        AdvertiseSettings advertiseSettingsBuild = builder.build();
        if (advertiseSettingsBuild == null) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "mAdvertiseSettings == null");
        }
        return advertiseSettingsBuild;
    }

    @RequiresApi(api = 21)
    public AdvertiseData a(byte[] bArr) {
        if (bArr == null) {
            a.a.a.a.b.m.a.c("AdvertiseManager", "createAdvertiseData: failed. payload is empty");
            return null;
        }
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
        builder.addManufacturerData(43009, bArr);
        AdvertiseData advertiseDataBuild = builder.build();
        if (advertiseDataBuild == null) {
            a.a.a.a.b.m.a.b("AdvertiseManager", "mAdvertiseSettings == null");
        }
        return advertiseDataBuild;
    }

    @RequiresApi(api = 21)
    public final void a(byte[] bArr, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
        BluetoothAdapter bluetoothAdapter;
        a.a.a.a.b.m.a.c("AdvertiseManager", " call startAdvertise ----" + ConvertUtils.bytes2HexString(bArr));
        if (this.f1183b != null && (bluetoothAdapter = this.f1185d) != null && bluetoothAdapter.isEnabled()) {
            AdvertiseData advertiseDataA = a(bArr);
            if (advertiseDataA == null) {
                bleAdvertiseCallback.onFailure(-1, "failed to create advertise data");
                return;
            }
            d();
            this.e.removeCallbacks(this.f);
            this.j = a(bleAdvertiseCallback);
            a.a.a.a.b.m.a.c("AdvertiseManager", "try to advertise----");
            if (Build.VERSION.SDK_INT >= 31) {
                Context context = this.m;
                if (context != null && Utils.checkBlePermission(context)) {
                    this.f1183b.startAdvertising(a(true, 60000), advertiseDataA, this.j);
                }
            } else {
                this.f1183b.startAdvertising(a(true, 60000), advertiseDataA, this.j);
            }
            this.e.postDelayed(this.f, 60000L);
            return;
        }
        a.a.a.a.b.m.a.b("AdvertiseManager", "failed start advertise");
        bleAdvertiseCallback.onFailure(-1, "failed to start advertise");
    }

    @RequiresApi(api = 21)
    public void a(byte[] bArr, int i, BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
        BluetoothAdapter bluetoothAdapter;
        a.a.a.a.b.m.a.c("AdvertiseManager", " call startAdvertise ----" + ConvertUtils.bytes2HexString(bArr));
        if (this.f1183b != null && (bluetoothAdapter = this.f1185d) != null && bluetoothAdapter.isEnabled()) {
            AdvertiseData advertiseDataA = a(bArr);
            if (advertiseDataA == null) {
                bleAdvertiseCallback.onFailure(-1, "failed to create advertise data");
                return;
            }
            d();
            this.e.removeCallbacks(this.f);
            this.j = a(bleAdvertiseCallback);
            a.a.a.a.b.m.a.c("AdvertiseManager", "try to advertise----");
            this.f1183b.startAdvertising(a(true, i), advertiseDataA, this.j);
            return;
        }
        a.a.a.a.b.m.a.b("AdvertiseManager", "failed start advertise");
        bleAdvertiseCallback.onFailure(-1, "failed to start advertise");
    }

    public synchronized void a(a.a.a.a.a.a.a.b.a aVar) {
        this.k.offer(aVar);
        if (!this.l) {
            a();
        } else {
            a.a.a.a.b.m.a.b("AdvertiseManager", "watting for advertising");
        }
    }

    @RequiresApi(api = 21)
    public final void a() {
        a.a.a.a.b.m.a.c("AdvertiseManager", "checkAndSendControlCmd");
        a.a.a.a.a.a.a.b.a aVarPoll = this.k.poll();
        if (aVarPoll != null) {
            this.h.b();
            this.h.a(aVarPoll);
            this.h.a(new c(this, aVarPoll));
            this.h.a(true);
            this.l = true;
            this.e.post(this.h);
            this.e.postDelayed(new d(this), 4000L);
            return;
        }
        a.a.a.a.b.m.a.c("AdvertiseManager", "no task in queue. return");
        this.l = false;
    }

    @RequiresApi(api = 21)
    public final AdvertiseCallback a(BleAdvertiseCallback<Boolean> bleAdvertiseCallback) {
        return new e(this, bleAdvertiseCallback);
    }
}

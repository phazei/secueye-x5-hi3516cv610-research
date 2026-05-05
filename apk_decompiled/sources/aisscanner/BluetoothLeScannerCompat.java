package aisscanner;

import aisscanner.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import com.alibaba.ailabs.iot.aisbase.C0433i;
import com.alibaba.ailabs.iot.aisbase.C0447p;
import com.alibaba.ailabs.iot.aisbase.C0449q;
import com.alibaba.ailabs.iot.aisbase.C0451r;
import com.alibaba.ailabs.iot.aisbase.RunnableC0419b;
import com.alibaba.ailabs.iot.aisbase.RunnableC0423d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class BluetoothLeScannerCompat {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static BluetoothLeScannerCompat f1589a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Handler f1590b = new Handler(Looper.getMainLooper());

    /* JADX INFO: Access modifiers changed from: package-private */
    public static class a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final boolean f1592b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final boolean f1593c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final boolean f1594d;

        @NonNull
        public final List<ScanFilter> f;

        @NonNull
        public final ScanSettings g;

        @NonNull
        public final ScanCallback h;

        @NonNull
        public final Handler i;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @NonNull
        public final Object f1591a = new Object();

        @NonNull
        public final List<ScanResult> j = new ArrayList();

        @NonNull
        public final Set<String> k = new HashSet();

        @NonNull
        public final Map<String, ScanResult> l = new HashMap();

        @NonNull
        public final Runnable m = new RunnableC0419b(this);

        @NonNull
        public final Runnable n = new RunnableC0423d(this);
        public boolean e = false;

        public a(boolean z, boolean z2, @NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler) {
            this.f = Collections.unmodifiableList(list);
            this.g = scanSettings;
            this.h = scanCallback;
            this.i = handler;
            boolean z3 = false;
            this.f1594d = (scanSettings.getCallbackType() == 1 || ((Build.VERSION.SDK_INT >= 23) && scanSettings.getUseHardwareCallbackTypesIfSupported())) ? false : true;
            this.f1592b = (list.isEmpty() || (z2 && scanSettings.getUseHardwareFilteringIfSupported())) ? false : true;
            long reportDelayMillis = scanSettings.getReportDelayMillis();
            if (reportDelayMillis > 0 && (!z || !scanSettings.getUseHardwareBatchingIfSupported())) {
                z3 = true;
            }
            this.f1593c = z3;
            if (this.f1593c) {
                handler.postDelayed(this.m, reportDelayMillis);
            }
        }

        public void a() {
            this.e = true;
            this.i.removeCallbacksAndMessages(null);
            synchronized (this.f1591a) {
                this.l.clear();
                this.k.clear();
                this.j.clear();
            }
        }

        public void b() {
            if (!this.f1593c || this.e) {
                return;
            }
            synchronized (this.f1591a) {
                this.h.onBatchScanResults(new ArrayList(this.j));
                this.j.clear();
                this.k.clear();
            }
        }

        public final boolean b(@NonNull ScanResult scanResult) {
            Iterator<ScanFilter> it = this.f.iterator();
            while (it.hasNext()) {
                if (it.next().matches(scanResult)) {
                    return true;
                }
            }
            return false;
        }

        public void a(@NonNull ScanResult scanResult) {
            boolean zIsEmpty;
            ScanResult scanResultPut;
            if (this.e) {
                return;
            }
            if (this.f.isEmpty() || b(scanResult)) {
                String address = scanResult.getDevice().getAddress();
                if (this.f1594d) {
                    synchronized (this.l) {
                        zIsEmpty = this.l.isEmpty();
                        scanResultPut = this.l.put(address, scanResult);
                    }
                    if (scanResultPut == null && (this.g.getCallbackType() & 2) > 0) {
                        this.h.onScanResult(2, scanResult);
                    }
                    if (!zIsEmpty || (this.g.getCallbackType() & 4) <= 0) {
                        return;
                    }
                    this.i.removeCallbacks(this.n);
                    this.i.postDelayed(this.n, this.g.getMatchLostTaskInterval());
                    return;
                }
                if (this.f1593c) {
                    synchronized (this.f1591a) {
                        if (!this.k.contains(address)) {
                            this.j.add(scanResult);
                            this.k.add(address);
                        }
                    }
                    return;
                }
                this.h.onScanResult(1, scanResult);
            }
        }

        public void a(@NonNull List<ScanResult> list) {
            if (this.e) {
                return;
            }
            if (this.f1592b) {
                ArrayList arrayList = new ArrayList();
                for (ScanResult scanResult : list) {
                    if (b(scanResult)) {
                        arrayList.add(scanResult);
                    }
                }
                list = arrayList;
            }
            this.h.onBatchScanResults(list);
        }

        public void a(int i) {
            this.h.onScanFailed(i);
        }
    }

    public static synchronized BluetoothLeScannerCompat getScanner() {
        BluetoothLeScannerCompat bluetoothLeScannerCompat = f1589a;
        if (bluetoothLeScannerCompat != null) {
            return bluetoothLeScannerCompat;
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            C0451r c0451r = new C0451r();
            f1589a = c0451r;
            return c0451r;
        }
        if (i >= 23) {
            C0449q c0449q = new C0449q();
            f1589a = c0449q;
            return c0449q;
        }
        if (i >= 21) {
            C0447p c0447p = new C0447p();
            f1589a = c0447p;
            return c0447p;
        }
        C0433i c0433i = new C0433i();
        f1589a = c0433i;
        return c0433i;
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public abstract void a(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler);

    public abstract void flushPendingScanResults(@NonNull ScanCallback scanCallback);

    public void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            this.f1590b.post(runnable);
        } else {
            runnable.run();
        }
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void startScan(@NonNull ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        a(Collections.emptyList(), new ScanSettings.Builder().build(), scanCallback, new Handler(Looper.getMainLooper()));
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public abstract void stopScan(@NonNull ScanCallback scanCallback);

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void startScan(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull ScanCallback scanCallback) {
        if (scanCallback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            if (list == null) {
                list = Collections.emptyList();
            }
            if (scanSettings == null) {
                scanSettings = new ScanSettings.Builder().build();
            }
            a(list, scanSettings, scanCallback, handler);
            return;
        }
        throw new IllegalArgumentException("callback is null");
    }

    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void startScan(@Nullable List<ScanFilter> list, @Nullable ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @Nullable Handler handler) {
        if (scanCallback != null) {
            if (list == null) {
                list = Collections.emptyList();
            }
            if (scanSettings == null) {
                scanSettings = new ScanSettings.Builder().build();
            }
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            a(list, scanSettings, scanCallback, handler);
            return;
        }
        throw new IllegalArgumentException("callback is null");
    }
}

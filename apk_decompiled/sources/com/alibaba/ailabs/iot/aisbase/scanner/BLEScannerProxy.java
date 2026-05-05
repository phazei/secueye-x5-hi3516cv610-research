package com.alibaba.ailabs.iot.aisbase.scanner;

import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanCallback;
import aisscanner.ScanResult;
import aisscanner.ScanSettings;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.ailabs.iot.aisbase.Ea;
import com.alibaba.ailabs.iot.aisbase.Fa;
import com.alibaba.ailabs.iot.aisbase.Ga;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.aisbase.utils.ThreadPool;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.hjq.permissions.Permission;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class BLEScannerProxy {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2644a = "BLEScannerProxy";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static BLEScannerProxy f2645b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static IMeshNetworkPUDListener f2646c;
    public ScanCallback g;
    public ScanCallback h;
    public c k;
    public c l;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Handler f2647d = new Handler(Looper.getMainLooper());
    public boolean e = false;
    public boolean f = false;
    public Context i = null;
    public Map<Integer, ILeScanStrategy> j = new ConcurrentHashMap();
    public ReentrantLock m = new ReentrantLock(false);
    public BroadcastReceiver n = null;
    public LocalBroadcastManager o = null;
    public final String p = "ACTION_SCAN_TOO_FREQUENTLY";
    public List<b> q = new ArrayList(6);
    public volatile boolean r = false;

    public interface IMeshNetworkPUDListener {
        void onMeshNetworkPDURecevied(ScanResult scanResult);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static class a extends ScanCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public boolean f2648a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public List<ILeScanStrategy> f2649b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public Set<ILeScanCallback> f2650c = new CopyOnWriteArraySet();

        public a(boolean z, List<ILeScanStrategy> list, ILeScanCallback iLeScanCallback) {
            this.f2648a = false;
            this.f2648a = z;
            this.f2649b = list;
            this.f2650c.add(iLeScanCallback);
        }

        public void a(ILeScanCallback iLeScanCallback) {
            if (this.f2650c.contains(iLeScanCallback)) {
                return;
            }
            LogUtils.i(BLEScannerProxy.f2644a, "addLeScanCallback: " + iLeScanCallback);
            this.f2650c.add(iLeScanCallback);
        }

        public boolean b(ILeScanCallback iLeScanCallback) {
            return this.f2650c.remove(iLeScanCallback);
        }

        public void c() {
            Iterator<ILeScanCallback> it = this.f2650c.iterator();
            while (it.hasNext()) {
                it.next().onStopScan();
            }
        }

        @Override // aisscanner.ScanCallback
        public void onBatchScanResults(@NonNull List<ScanResult> list) {
            super.onBatchScanResults(list);
            Iterator<ScanResult> it = list.iterator();
            while (it.hasNext()) {
                a(it.next());
            }
        }

        @Override // aisscanner.ScanCallback
        public void onScanFailed(int i) {
            super.onScanFailed(i);
            LogUtils.e(BLEScannerProxy.f2644a, String.format("scan failed, error code: %d", Integer.valueOf(i)));
        }

        @Override // aisscanner.ScanCallback
        public void onScanResult(int i, @NonNull ScanResult scanResult) {
            super.onScanResult(i, scanResult);
            a(scanResult);
        }

        public void b() {
            this.f2650c.clear();
        }

        public void a(ILeScanStrategy iLeScanStrategy) {
            if (this.f2649b.contains(iLeScanStrategy)) {
                LogUtils.w(BLEScannerProxy.f2644a, "addStrategy: strategy has exist");
            } else {
                this.f2649b.add(iLeScanStrategy);
                LogUtils.i(BLEScannerProxy.f2644a, "addStrategy success");
            }
        }

        public boolean a() {
            return this.f2650c.size() == 0;
        }

        public final synchronized void a(ScanResult scanResult) {
            if (scanResult != null) {
                if (scanResult.getScanRecord() != null) {
                    if (scanResult.getScanRecord().getMeshNetworkPUD() != null) {
                        if (BLEScannerProxy.f2646c != null) {
                            ThreadPool.execute(new Ga(this, scanResult));
                        }
                        return;
                    }
                    scanResult.getScanRecord().getManufacturerSpecificData(424);
                    for (ILeScanStrategy iLeScanStrategy : this.f2649b) {
                        BluetoothDeviceWrapper bluetoothDeviceWrapperCreateFromScanResult = iLeScanStrategy.createFromScanResult(scanResult);
                        if (bluetoothDeviceWrapperCreateFromScanResult != null) {
                            Iterator it = new CopyOnWriteArraySet(this.f2650c).iterator();
                            while (it.hasNext()) {
                                ((ILeScanCallback) it.next()).onAliBLEDeviceFound(bluetoothDeviceWrapperCreateFromScanResult, iLeScanStrategy.getBluetoothDeviceSubtype());
                            }
                        }
                    }
                }
            }
        }
    }

    private class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public long f2651a;

        public b(long j) {
            this.f2651a = j;
        }
    }

    private class c implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public ILeScanCallback f2653a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public ScanCallback f2654b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public boolean f2655c;

        public c(ILeScanCallback iLeScanCallback, ScanCallback scanCallback, boolean z) {
            this.f2653a = iLeScanCallback;
            this.f2654b = scanCallback;
            this.f2655c = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            LogUtils.d(BLEScannerProxy.f2644a, "Scan timeout task trigger");
            if (this.f2655c) {
                BLEScannerProxy.this.stopScan(this.f2653a);
            } else {
                BLEScannerProxy.this.stopDirectionalScan();
            }
        }
    }

    public static BLEScannerProxy getInstance() {
        if (f2645b == null) {
            synchronized (BLEScannerProxy.class) {
                if (f2645b == null) {
                    f2645b = new BLEScannerProxy();
                }
            }
        }
        return f2645b;
    }

    public static boolean isBleEnabled() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        return defaultAdapter != null && defaultAdapter.isEnabled();
    }

    public static boolean isLocationPermissionsGranted(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? ContextCompat.checkSelfPermission(context, Permission.NEARBY_WIFI_DEVICES) == 0 : ContextCompat.checkSelfPermission(context, Permission.ACCESS_COARSE_LOCATION) == 0;
    }

    public final void c() {
        this.n = new Ea(this);
    }

    public boolean checkIfInScanning() {
        return this.e;
    }

    public boolean checkPermission(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return Build.VERSION.SDK_INT < 23 ? context.getPackageManager().checkPermission(str, context.getPackageName()) == 0 : ContextCompat.checkSelfPermission(context, str) == 0;
    }

    public final boolean d() {
        return this.q.size() >= 5 && System.currentTimeMillis() - this.q.get(0).f2651a < 30000;
    }

    public ScanCallback getDirectionalScanCallback(ILeScanStrategy iLeScanStrategy, ILeScanCallback iLeScanCallback) {
        this.m.lock();
        try {
            if (this.h != null) {
                return this.h;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(iLeScanStrategy);
            this.h = new a(false, arrayList, iLeScanCallback);
            return this.h;
        } finally {
            this.m.unlock();
        }
    }

    public ScanCallback getScanCallback(List<ILeScanStrategy> list, ILeScanCallback iLeScanCallback) {
        this.m.lock();
        try {
            if (this.g == null) {
                this.g = new Fa(this, true, list, iLeScanCallback);
                return this.g;
            }
            Iterator<ILeScanStrategy> it = list.iterator();
            while (it.hasNext()) {
                ((a) this.g).a(it.next());
            }
            ((a) this.g).a(iLeScanCallback);
            return this.g;
        } finally {
            this.m.unlock();
        }
    }

    public boolean isBleScanPermissionGranted(Context context) {
        if (context == null) {
            return false;
        }
        Context applicationContext = context.getApplicationContext();
        int i = applicationContext instanceof Application ? applicationContext.getApplicationInfo().targetSdkVersion : 0;
        if (i < 31 || Build.VERSION.SDK_INT < 31) {
            return true;
        }
        LogUtils.d(f2644a, "appTargetSdk = " + i);
        if (checkPermission(applicationContext, Permission.BLUETOOTH_SCAN)) {
            return true;
        }
        LogUtils.w(f2644a, "app target sdk = 31 and not BLUETOOTH_SCAN permission granted, return.");
        return false;
    }

    public void lock() {
        this.r = true;
    }

    public boolean registerLeScanStrategy(int i, ILeScanStrategy iLeScanStrategy) {
        LogUtils.d(f2644a, "Register device type: " + i);
        if (this.j.get(Integer.valueOf(i)) != null) {
            LogUtils.w(f2644a, "The device type has been registered");
            return false;
        }
        this.j.put(Integer.valueOf(i), iLeScanStrategy);
        this.m.lock();
        try {
            if (this.g != null) {
                ((a) this.g).a(iLeScanStrategy);
            }
            this.m.unlock();
            return true;
        } catch (Throwable th) {
            this.m.unlock();
            throw th;
        }
    }

    public void setOnMeshNetworkPUDListener(IMeshNetworkPUDListener iMeshNetworkPUDListener) {
        f2646c = iMeshNetworkPUDListener;
    }

    public ScanCallback startDirectionalLeScan(Context context, int i, String[] strArr, ILeScanStrategy iLeScanStrategy, ILeScanCallback iLeScanCallback) {
        if (this.r) {
            LogUtils.w(f2644a, "Scan not allowed");
            return null;
        }
        LogUtils.d(f2644a, "Start performing a directional scan[" + TextUtils.join(",", strArr) + "]");
        if (this.f) {
            if (i != 0) {
                this.f2647d.removeCallbacks(this.l);
                this.f2647d.postDelayed(this.l, i);
            }
            c cVar = this.l;
            if (cVar == null) {
                return null;
            }
            return cVar.f2654b;
        }
        if (d()) {
            LogUtils.i(f2644a, "Scanning too frequently: ACTION_SCAN_TOO_FREQUENTLY");
            this.o = LocalBroadcastManager.getInstance(context);
            this.o.sendBroadcast(new Intent("ACTION_SCAN_TOO_FREQUENTLY"));
            return null;
        }
        this.f = true;
        this.i = context.getApplicationContext();
        if (this.n == null) {
            c();
            this.i.registerReceiver(this.n, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        }
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(2).setUseHardwareFilteringIfSupported(true).build();
        ArrayList arrayList = new ArrayList();
        ScanCallback directionalScanCallback = getDirectionalScanCallback(iLeScanStrategy, iLeScanCallback);
        try {
            BluetoothLeScannerCompat.getScanner().startScan(arrayList, scanSettingsBuild, directionalScanCallback);
            this.q.add(new b(System.currentTimeMillis()));
            if (iLeScanCallback != null) {
                iLeScanCallback.onStartScan();
            }
            if (i > 0) {
                this.l = new c(iLeScanCallback, directionalScanCallback, false);
                this.f2647d.postDelayed(this.l, i);
            }
            this.q.add(new b(System.currentTimeMillis()));
            LogUtils.v(f2644a, "Start up system scanner success");
            return directionalScanCallback;
        } catch (IllegalArgumentException e) {
            e = e;
            LogUtils.e(f2644a, e.toString());
            return directionalScanCallback;
        } catch (IllegalStateException e2) {
            e = e2;
            LogUtils.e(f2644a, e.toString());
            return directionalScanCallback;
        } catch (Throwable th) {
            LogUtils.e(f2644a, "catch ex(t=31?)=" + th.toString());
            return directionalScanCallback;
        }
    }

    public ScanCallback startLeScan(Context context, int i, boolean z, int i2, ILeScanCallback iLeScanCallback) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        if (this.r) {
            LogUtils.w(f2644a, "Scan not allowed");
            return null;
        }
        LogUtils.d(f2644a, "start scan, current in scanning: " + this.e + ", scan callback: " + iLeScanCallback);
        if (!isBleEnabled()) {
            LogUtils.w(f2644a, "Bluetooth not enable");
            return null;
        }
        if (!isBleScanPermissionGranted(context)) {
            LogUtils.w(f2644a, "Bluetooth scan permission not enable for target sdk 31.");
            return null;
        }
        if (!isLocationPermissionsGranted(context)) {
            LogUtils.w(f2644a, "Location permission is not granted");
            return null;
        }
        this.m.lock();
        try {
            Set<Integer> setKeySet = this.j.keySet();
            if (this.e) {
                if (i != 0) {
                    this.f2647d.removeCallbacks(this.k);
                    this.f2647d.postDelayed(this.k, i);
                }
                if (this.g != null) {
                    String str = f2644a;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Add new scan callback: ");
                    sb.append(iLeScanCallback);
                    LogUtils.i(str, sb.toString());
                    ((a) this.g).a(iLeScanCallback);
                    Iterator<Integer> it = setKeySet.iterator();
                    while (it.hasNext()) {
                        int iIntValue = it.next().intValue();
                        if ((i2 & iIntValue) == iIntValue && this.j.containsKey(Integer.valueOf(iIntValue))) {
                            String str2 = f2644a;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Prepare add scan strategy for device type: ");
                            sb2.append(iIntValue);
                            LogUtils.v(str2, sb2.toString());
                            ((a) this.g).a(this.j.get(Integer.valueOf(iIntValue)));
                        }
                    }
                }
                if (iLeScanCallback != null) {
                    iLeScanCallback.onStartScan();
                }
                return this.g;
            }
            if (d()) {
                LogUtils.i(f2644a, "Scanning too frequently: ACTION_SCAN_TOO_FREQUENTLY");
                this.o = LocalBroadcastManager.getInstance(context);
                this.o.sendBroadcast(new Intent("ACTION_SCAN_TOO_FREQUENTLY"));
                return null;
            }
            List<ILeScanStrategy> copyOnWriteArrayList2 = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList copyOnWriteArrayList3 = new CopyOnWriteArrayList();
            Iterator<Integer> it2 = setKeySet.iterator();
            while (it2.hasNext()) {
                int iIntValue2 = it2.next().intValue();
                if ((i2 & iIntValue2) == iIntValue2 && this.j.containsKey(Integer.valueOf(iIntValue2))) {
                    String str3 = f2644a;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Add scan strategy for device type: ");
                    sb3.append(iIntValue2);
                    LogUtils.v(str3, sb3.toString());
                    copyOnWriteArrayList2.add(this.j.get(Integer.valueOf(iIntValue2)));
                    copyOnWriteArrayList3.addAll(this.j.get(Integer.valueOf(iIntValue2)).getCustomScanFilters());
                }
            }
            this.e = true;
            this.i = context.getApplicationContext();
            if (this.n == null) {
                c();
                this.i.registerReceiver(this.n, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            }
            ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(1).setUseHardwareFilteringIfSupported(true).build();
            if (z) {
                copyOnWriteArrayList = new CopyOnWriteArrayList();
                if (copyOnWriteArrayList3.size() > 0) {
                    copyOnWriteArrayList.addAll(copyOnWriteArrayList3);
                }
            } else {
                copyOnWriteArrayList = null;
            }
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            ScanCallback scanCallback = getScanCallback(copyOnWriteArrayList2, iLeScanCallback);
            try {
                scanner.startScan(copyOnWriteArrayList, scanSettingsBuild, scanCallback);
                if (iLeScanCallback != null) {
                    iLeScanCallback.onStartScan();
                }
                this.k = new c(iLeScanCallback, scanCallback, true);
                if (i > 0) {
                    this.f2647d.postDelayed(this.k, i);
                }
                this.q.add(new b(System.currentTimeMillis()));
                LogUtils.i(f2644a, "Start up system scanner success");
                return scanCallback;
            } catch (IllegalArgumentException | IllegalStateException e) {
                this.e = false;
                this.g = null;
                String str4 = f2644a;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Start up system scanner failed: ");
                sb4.append(e.toString());
                LogUtils.e(str4, sb4.toString());
                return null;
            }
        } finally {
            this.m.unlock();
        }
    }

    public void stopDirectionalScan() {
        this.m.lock();
        try {
            String str = f2644a;
            StringBuilder sb = new StringBuilder();
            sb.append("Stop directional scan, current in scanning: ");
            sb.append(this.f);
            LogUtils.d(str, sb.toString());
            if (this.f && this.h != null) {
                this.f2647d.removeCallbacks(this.l);
                try {
                    BluetoothLeScannerCompat.getScanner().stopScan(this.h);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (this.q.size() > 5) {
                    this.q.remove(0);
                    LogUtils.i(f2644a, "Update timestamp history");
                }
                ((a) this.h).c();
                this.h = null;
                this.f = false;
            }
        } finally {
            this.m.unlock();
        }
    }

    public boolean stopScan(ILeScanCallback iLeScanCallback) {
        LogUtils.i(f2644a, "Stop scan with callback: " + iLeScanCallback);
        this.m.lock();
        try {
            String str = f2644a;
            StringBuilder sb = new StringBuilder();
            sb.append("stop scan, current in scanning: ");
            sb.append(this.e);
            LogUtils.d(str, sb.toString());
            if (this.e && this.g != null) {
                a aVar = (a) this.g;
                if (iLeScanCallback != null) {
                    iLeScanCallback.onStopScan();
                }
                aVar.b(iLeScanCallback);
                if (aVar.a()) {
                    this.f2647d.removeCallbacks(this.k);
                    BluetoothLeScannerCompat.getScanner().stopScan(this.g);
                    if (this.q.size() > 5) {
                        this.q.remove(0);
                        LogUtils.i(f2644a, "Update timestamp history");
                    }
                    this.e = false;
                    this.g = null;
                    LogUtils.i(f2644a, "Stop system scanner success");
                }
                this.m.unlock();
                return true;
            }
            return false;
        } finally {
            this.m.unlock();
        }
    }

    public void unlock() {
        this.r = false;
    }

    public boolean stopScan() {
        this.m.lock();
        try {
            String str = f2644a;
            StringBuilder sb = new StringBuilder();
            sb.append("stop scan, current in scanning: ");
            sb.append(this.e);
            LogUtils.d(str, sb.toString());
            if (this.e && this.g != null) {
                this.f2647d.removeCallbacks(this.k);
                BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
                a aVar = (a) this.g;
                aVar.c();
                aVar.b();
                try {
                    scanner.stopScan(this.g);
                    LogUtils.i(f2644a, "Stop system scanner success");
                } catch (Exception e) {
                    String str2 = f2644a;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Stop system scanner failed: ");
                    sb2.append(e.toString());
                    LogUtils.e(str2, sb2.toString());
                }
                if (this.q.size() > 5) {
                    this.q.remove(0);
                    LogUtils.i(f2644a, "Update timestamp history");
                }
                this.e = false;
                this.m.unlock();
                return true;
            }
            return false;
        } finally {
            this.m.unlock();
        }
    }

    public ScanCallback startLeScan(Context context, int i, boolean z, int i2, ILeScanCallback iLeScanCallback, ScanSettings scanSettings) {
        ArrayList arrayList;
        if (this.r) {
            LogUtils.w(f2644a, "Scan not allowed");
            return null;
        }
        LogUtils.d(f2644a, "start scan, current in scanning: " + this.e);
        if (!isBleEnabled()) {
            LogUtils.w(f2644a, "Bluetooth not enable");
            return null;
        }
        if (!isLocationPermissionsGranted(context)) {
            LogUtils.w(f2644a, "Location permission is not granted");
            return null;
        }
        if (!isBleScanPermissionGranted(context)) {
            LogUtils.w(f2644a, "Bluetooth scan permission not enable for target sdk 31.");
            return null;
        }
        this.m.lock();
        try {
            if (this.e) {
                if (i != 0) {
                    this.f2647d.removeCallbacks(this.k);
                    this.f2647d.postDelayed(this.k, i);
                }
                if (this.g != null) {
                    ((a) this.g).a(iLeScanCallback);
                }
                if (iLeScanCallback != null) {
                    iLeScanCallback.onStartScan();
                }
                return this.g;
            }
            if (d()) {
                LogUtils.i(f2644a, "Scanning too frequently: ACTION_SCAN_TOO_FREQUENTLY");
                this.o = LocalBroadcastManager.getInstance(context);
                this.o.sendBroadcast(new Intent("ACTION_SCAN_TOO_FREQUENTLY"));
                return null;
            }
            Set<Integer> setKeySet = this.j.keySet();
            List<ILeScanStrategy> arrayList2 = new ArrayList<>();
            ArrayList arrayList3 = new ArrayList();
            Iterator<Integer> it = setKeySet.iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                if ((i2 & iIntValue) == iIntValue && this.j.containsKey(Integer.valueOf(iIntValue))) {
                    String str = f2644a;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Add scan strategy for device type: ");
                    sb.append(iIntValue);
                    LogUtils.v(str, sb.toString());
                    arrayList2.add(this.j.get(Integer.valueOf(iIntValue)));
                    arrayList3.addAll(this.j.get(Integer.valueOf(iIntValue)).getCustomScanFilters());
                }
            }
            this.e = true;
            this.i = context.getApplicationContext();
            if (this.n == null) {
                c();
                this.i.registerReceiver(this.n, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            }
            if (z) {
                arrayList = new ArrayList();
                if (arrayList3.size() > 0) {
                    arrayList.addAll(arrayList3);
                }
            } else {
                arrayList = null;
            }
            BluetoothLeScannerCompat scanner = BluetoothLeScannerCompat.getScanner();
            ScanCallback scanCallback = getScanCallback(arrayList2, iLeScanCallback);
            try {
                scanner.startScan(arrayList, scanSettings, scanCallback);
                if (iLeScanCallback != null) {
                    iLeScanCallback.onStartScan();
                }
                this.k = new c(iLeScanCallback, scanCallback, true);
                if (i > 0) {
                    this.f2647d.postDelayed(this.k, i);
                }
                this.q.add(new b(System.currentTimeMillis()));
                LogUtils.v(f2644a, "Start up system scanner success");
                return scanCallback;
            } catch (IllegalArgumentException | IllegalStateException e) {
                this.e = false;
                this.g = null;
                LogUtils.e(f2644a, e.toString());
                return null;
            }
        } finally {
            this.m.unlock();
        }
    }
}

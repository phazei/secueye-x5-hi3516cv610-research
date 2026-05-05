package com.alibaba.ailabs.iot.aisbase;

import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanCallback;
import aisscanner.ScanFilter;
import aisscanner.ScanSettings;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.i, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplJB.java */
/* JADX INFO: loaded from: classes.dex */
public class C0433i extends BluetoothLeScannerCompat {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @Nullable
    public HandlerThread f2585d;

    @Nullable
    public Handler e;
    public long f;
    public long g;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @NonNull
    public final Map<ScanCallback, BluetoothLeScannerCompat.a> f2584c = new HashMap();
    public final Runnable h = new RunnableC0425e(this);
    public final Runnable i = new RunnableC0427f(this);
    public final BluetoothAdapter.LeScanCallback j = new C0431h(this);

    @Override // aisscanner.BluetoothLeScannerCompat
    @RequiresPermission("android.permission.BLUETOOTH")
    public void flushPendingScanResults(@NonNull ScanCallback scanCallback) {
        BluetoothLeScannerCompat.a aVar;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || defaultAdapter.getState() != 12) {
            return;
        }
        if (scanCallback == null) {
            throw new IllegalArgumentException("callback cannot be null!");
        }
        synchronized (this.f2584c) {
            aVar = this.f2584c.get(scanCallback);
        }
        if (aVar == null) {
            throw new IllegalArgumentException("callback not registered!");
        }
        aVar.b();
    }

    @Override // aisscanner.BluetoothLeScannerCompat
    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    public void stopScan(@NonNull ScanCallback scanCallback) {
        if (scanCallback == null) {
            throw new IllegalArgumentException("scanCallback cannot be null!");
        }
        synchronized (this.f2584c) {
            BluetoothLeScannerCompat.a aVar = this.f2584c.get(scanCallback);
            if (aVar == null) {
                return;
            }
            this.f2584c.remove(scanCallback);
            boolean zIsEmpty = this.f2584c.isEmpty();
            aVar.a();
            a();
            if (zIsEmpty) {
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (defaultAdapter != null) {
                    defaultAdapter.stopLeScan(this.j);
                }
                Handler handler = this.e;
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                HandlerThread handlerThread = this.f2585d;
                if (handlerThread != null) {
                    handlerThread.quitSafely();
                    this.f2585d = null;
                }
            }
        }
    }

    @Override // aisscanner.BluetoothLeScannerCompat
    @RequiresPermission(allOf = {"android.permission.BLUETOOTH_ADMIN", "android.permission.BLUETOOTH"})
    public void a(@NonNull List<ScanFilter> list, @NonNull ScanSettings scanSettings, @NonNull ScanCallback scanCallback, @NonNull Handler handler) {
        boolean zIsEmpty;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || defaultAdapter.getState() != 12) {
            return;
        }
        synchronized (this.f2584c) {
            if (this.f2584c.containsKey(scanCallback)) {
                throw new IllegalArgumentException("scanner already started with given scanCallback");
            }
            BluetoothLeScannerCompat.a aVar = new BluetoothLeScannerCompat.a(false, false, list, scanSettings, scanCallback, handler);
            zIsEmpty = this.f2584c.isEmpty();
            this.f2584c.put(scanCallback, aVar);
        }
        if (this.f2585d == null) {
            this.f2585d = new HandlerThread(C0433i.class.getName());
            this.f2585d.start();
            this.e = new Handler(this.f2585d.getLooper());
        }
        a();
        if (zIsEmpty) {
            defaultAdapter.startLeScan(this.j);
        }
    }

    public final void a() {
        long powerSaveRest;
        long powerSaveScan;
        synchronized (this.f2584c) {
            Iterator<BluetoothLeScannerCompat.a> it = this.f2584c.values().iterator();
            powerSaveRest = Long.MAX_VALUE;
            powerSaveScan = Long.MAX_VALUE;
            while (it.hasNext()) {
                ScanSettings scanSettings = it.next().g;
                if (scanSettings.hasPowerSaveMode()) {
                    if (powerSaveRest > scanSettings.getPowerSaveRest()) {
                        powerSaveRest = scanSettings.getPowerSaveRest();
                    }
                    if (powerSaveScan > scanSettings.getPowerSaveScan()) {
                        powerSaveScan = scanSettings.getPowerSaveScan();
                    }
                }
            }
        }
        if (powerSaveRest < Long.MAX_VALUE && powerSaveScan < Long.MAX_VALUE) {
            this.f = powerSaveRest;
            this.g = powerSaveScan;
            Handler handler = this.e;
            if (handler != null) {
                handler.removeCallbacks(this.i);
                this.e.removeCallbacks(this.h);
                this.e.postDelayed(this.h, this.g);
                return;
            }
            return;
        }
        this.g = 0L;
        this.f = 0L;
        Handler handler2 = this.e;
        if (handler2 != null) {
            handler2.removeCallbacks(this.i);
            this.e.removeCallbacks(this.h);
        }
    }
}

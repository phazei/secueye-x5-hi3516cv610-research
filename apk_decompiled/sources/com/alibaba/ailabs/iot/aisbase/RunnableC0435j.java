package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanSettings;
import com.alibaba.ailabs.iot.aisbase.C0447p;
import java.util.List;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.j, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0435j implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothLeScanner f2589a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ List f2590b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ ScanSettings f2591c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ C0447p.a f2592d;
    public final /* synthetic */ C0447p e;

    public RunnableC0435j(C0447p c0447p, BluetoothLeScanner bluetoothLeScanner, List list, ScanSettings scanSettings, C0447p.a aVar) {
        this.e = c0447p;
        this.f2589a = bluetoothLeScanner;
        this.f2590b = list;
        this.f2591c = scanSettings;
        this.f2592d = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2589a.startScan(this.f2590b, this.f2591c, this.f2592d.o);
    }
}

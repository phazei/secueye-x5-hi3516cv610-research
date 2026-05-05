package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.le.BluetoothLeScanner;
import com.alibaba.ailabs.iot.aisbase.C0447p;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.k, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0437k implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothLeScanner f2595a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0447p.a f2596b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ C0447p f2597c;

    public RunnableC0437k(C0447p c0447p, BluetoothLeScanner bluetoothLeScanner, C0447p.a aVar) {
        this.f2597c = c0447p;
        this.f2595a = bluetoothLeScanner;
        this.f2596b = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.f2595a.stopScan(this.f2596b.o);
        } catch (Exception unused) {
        }
    }
}

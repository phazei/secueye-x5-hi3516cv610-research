package com.alibaba.ailabs.iot.aisbase;

import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanResult;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.g, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplJB.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0429g implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothLeScannerCompat.a f2578a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ScanResult f2579b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ C0431h f2580c;

    public RunnableC0429g(C0431h c0431h, BluetoothLeScannerCompat.a aVar, ScanResult scanResult) {
        this.f2580c = c0431h;
        this.f2578a = aVar;
        this.f2579b = scanResult;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2578a.a(this.f2579b);
    }
}

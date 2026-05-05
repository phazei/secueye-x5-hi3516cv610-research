package com.alibaba.ailabs.iot.aisbase;

import aisscanner.ScanResult;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.c, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerCompat.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0421c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ScanResult f2553a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ RunnableC0423d f2554b;

    public RunnableC0421c(RunnableC0423d runnableC0423d, ScanResult scanResult) {
        this.f2554b = runnableC0423d;
        this.f2553a = scanResult;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2554b.f2566a.h.onScanResult(4, this.f2553a);
    }
}

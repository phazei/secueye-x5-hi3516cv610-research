package com.alibaba.ailabs.iot.aisbase;

import aisscanner.BluetoothLeScannerCompat;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.b, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerCompat.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0419b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BluetoothLeScannerCompat.a f2551a;

    public RunnableC0419b(BluetoothLeScannerCompat.a aVar) {
        this.f2551a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2551a.e) {
            return;
        }
        this.f2551a.b();
        BluetoothLeScannerCompat.a aVar = this.f2551a;
        aVar.i.postDelayed(this, aVar.g.getReportDelayMillis());
    }
}

package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import com.alibaba.ailabs.iot.aisbase.C0447p;
import java.util.List;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.o, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class C0445o extends ScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public long f2611a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0447p.a f2612b;

    public C0445o(C0447p.a aVar) {
        this.f2612b = aVar;
    }

    @Override // android.bluetooth.le.ScanCallback
    public void onBatchScanResults(List<ScanResult> list) {
        this.f2612b.i.post(new RunnableC0441m(this, list));
    }

    @Override // android.bluetooth.le.ScanCallback
    public void onScanFailed(int i) {
        this.f2612b.i.post(new RunnableC0443n(this, i));
    }

    @Override // android.bluetooth.le.ScanCallback
    public void onScanResult(int i, ScanResult scanResult) {
        this.f2612b.i.post(new RunnableC0439l(this, scanResult));
    }
}

package com.alibaba.ailabs.iot.aisbase;

import android.bluetooth.le.ScanResult;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: renamed from: com.alibaba.ailabs.iot.aisbase.l, reason: case insensitive filesystem */
/* JADX INFO: compiled from: BluetoothLeScannerImplLollipop.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0439l implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ScanResult f2600a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ C0445o f2601b;

    public RunnableC0439l(C0445o c0445o, ScanResult scanResult) {
        this.f2601b = c0445o;
        this.f2600a = scanResult;
    }

    @Override // java.lang.Runnable
    public void run() {
        LogUtils.d("BluetoothLeScannerImplLollipop", "native scan bluetooth device: " + this.f2600a.getDevice().getAddress());
        this.f2601b.f2612b.a(C0447p.this.a(this.f2600a));
    }
}

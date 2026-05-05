package com.alibaba.ailabs.iot.aisbase;

import aisscanner.ScanResult;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;

/* JADX INFO: compiled from: BLEScannerProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Ga implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ScanResult f2481a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BLEScannerProxy.a f2482b;

    public Ga(BLEScannerProxy.a aVar, ScanResult scanResult) {
        this.f2482b = aVar;
        this.f2481a = scanResult;
    }

    @Override // java.lang.Runnable
    public void run() {
        BLEScannerProxy.f2646c.onMeshNetworkPDURecevied(this.f2481a);
    }
}

package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import java.util.List;

/* JADX INFO: compiled from: BLEScannerProxy.java */
/* JADX INFO: loaded from: classes.dex */
public class Fa extends BLEScannerProxy.a {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ BLEScannerProxy f2477d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Fa(BLEScannerProxy bLEScannerProxy, boolean z, List list, ILeScanCallback iLeScanCallback) {
        super(z, list, iLeScanCallback);
        this.f2477d = bLEScannerProxy;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy.a, aisscanner.ScanCallback
    public void onScanFailed(int i) {
        super.onScanFailed(i);
        this.f2477d.stopScan();
    }
}

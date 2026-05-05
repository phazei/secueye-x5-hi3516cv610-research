package b;

import aisscanner.ScanCallback;
import aisscanner.ScanResult;
import java.util.List;

/* JADX INFO: compiled from: SIGMeshNetworkTransportManager.java */
/* JADX INFO: loaded from: classes.dex */
public class w extends ScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ y f2221a;

    public w(y yVar) {
        this.f2221a = yVar;
    }

    @Override // aisscanner.ScanCallback
    public void onBatchScanResults(List<ScanResult> list) {
    }

    @Override // aisscanner.ScanCallback
    public void onScanFailed(int i) {
        a.a.a.a.b.m.a.b("SIGMeshNetworkTransportManager", "onScanFailed: " + i);
        this.f2221a.f2225c = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x0172  */
    @Override // aisscanner.ScanCallback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onScanResult(int r11, aisscanner.ScanResult r12) {
        /*
            Method dump skipped, instruction units count: 596
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: b.w.onScanResult(int, aisscanner.ScanResult):void");
    }
}

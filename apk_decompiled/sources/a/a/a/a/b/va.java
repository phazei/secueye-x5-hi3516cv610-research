package a.a.a.a.b;

import aisscanner.ScanCallback;
import aisscanner.ScanResult;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import java.util.List;

/* JADX INFO: compiled from: TgScanManager.java */
/* JADX INFO: loaded from: classes.dex */
public class va extends ScanCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TgScanManager f1552a;

    public va(TgScanManager tgScanManager) {
        this.f1552a = tgScanManager;
    }

    @Override // aisscanner.ScanCallback
    public void onBatchScanResults(@NonNull List<ScanResult> list) {
    }

    @Override // aisscanner.ScanCallback
    public void onScanFailed(int i) {
        this.f1552a.mScanner.scanningStopped();
        if (this.f1552a.mScanHandler != null) {
            this.f1552a.mScanHandler.onScanFailed(-6, "onScanFailed: " + i);
        }
        this.f1552a.loge("onScanFailed: " + i);
    }

    @Override // aisscanner.ScanCallback
    public void onScanResult(int i, @NonNull ScanResult scanResult) {
        if (this.f1552a.mScanner.isStopScanRequested()) {
            return;
        }
        if (this.f1552a.mScanHandler != null) {
            this.f1552a.mScanHandler.onScanResult(scanResult, this.f1552a.mScanner);
        }
        a.a.a.a.b.m.a.a(TgScanManager.TAG, "Scaned devices size: " + this.f1552a.mScanner.getDevices().size());
    }
}

package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.TgScanManager;

/* JADX INFO: compiled from: TgScanManager.java */
/* JADX INFO: loaded from: classes.dex */
public class ya implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TgScanManager f1558a;

    public ya(TgScanManager tgScanManager) {
        this.f1558a = tgScanManager;
    }

    @Override // java.lang.Runnable
    public void run() {
        TgScanManager tgScanManager = this.f1558a;
        tgScanManager.stopScan(tgScanManager.mContext);
        this.f1558a.logd("scan timeout");
    }
}

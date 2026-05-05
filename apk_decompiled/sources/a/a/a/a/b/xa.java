package a.a.a.a.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.ailabs.iot.mesh.TgScanManager;

/* JADX INFO: compiled from: TgScanManager.java */
/* JADX INFO: loaded from: classes.dex */
public class xa extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TgScanManager f1556a;

    public xa(TgScanManager tgScanManager) {
        this.f1556a = tgScanManager;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
        int intExtra2 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 10);
        if (intExtra != 10) {
            if (intExtra == 12) {
                this.f1556a.mScanner.bluetoothEnabled();
                a.a.a.a.b.m.a.a(TgScanManager.TAG, "bluetoothEnabled");
                return;
            } else if (intExtra != 13) {
                return;
            }
        }
        if (intExtra2 == 13 || intExtra2 == 10) {
            return;
        }
        TgScanManager tgScanManager = this.f1556a;
        tgScanManager.stopScan(tgScanManager.mContext);
        this.f1556a.mScanner.bluetoothDisabled();
        if (this.f1556a.mScanHandler != null) {
            this.f1556a.mScanHandler.onScanFailed(-6, "bluetooth_not_enabled");
        }
        this.f1556a.logw("bluetoothDisabled");
    }
}

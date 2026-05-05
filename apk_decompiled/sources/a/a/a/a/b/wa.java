package a.a.a.a.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;

/* JADX INFO: compiled from: TgScanManager.java */
/* JADX INFO: loaded from: classes.dex */
public class wa extends BroadcastReceiver {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ TgScanManager f1554a;

    public wa(TgScanManager tgScanManager) {
        this.f1554a = tgScanManager;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        boolean zIsLocationEnabled = Utils.isLocationEnabled(context);
        this.f1554a.mScanner.setLocationEnabled(zIsLocationEnabled);
        if (zIsLocationEnabled) {
            return;
        }
        if (this.f1554a.mScanHandler != null) {
            this.f1554a.mScanHandler.onScanFailed(-6, "location_not_enabled");
        }
        this.f1554a.logw("location is not enabled");
    }
}

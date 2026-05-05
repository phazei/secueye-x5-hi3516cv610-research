package a.a.a.a.a;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import com.alibaba.ailabs.iot.bleadvertise.callback.BleAdvertiseCallback;

/* JADX INFO: compiled from: AdvertiseManager.java */
/* JADX INFO: loaded from: classes.dex */
public class e extends AdvertiseCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ BleAdvertiseCallback f1179a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ g f1180b;

    public e(g gVar, BleAdvertiseCallback bleAdvertiseCallback) {
        this.f1180b = gVar;
        this.f1179a = bleAdvertiseCallback;
    }

    @Override // android.bluetooth.le.AdvertiseCallback
    public void onStartFailure(int i) {
        super.onStartFailure(i);
        a.a.a.a.b.m.a.b("AdvertiseManager", "onStartFailure errorCode" + i);
        String str = "";
        if (i == 1) {
            str = "Failed to start advertising as the advertise data to be broadcasted is larger than 31 bytes.";
        } else if (i == 2) {
            str = "Failed to start advertising because no advertising instance is available.";
        } else if (i == 3) {
            str = "Failed to start advertising as the advertising is already started";
        } else if (i == 4) {
            str = "Operation failed due to an internal error";
        } else if (i == 5) {
            str = "This feature is not supported on this platform";
        }
        a.a.a.a.b.m.a.b("AdvertiseManager", str);
        this.f1179a.onFailure(i, str);
    }

    @Override // android.bluetooth.le.AdvertiseCallback
    public void onStartSuccess(AdvertiseSettings advertiseSettings) {
        super.onStartSuccess(advertiseSettings);
        if (advertiseSettings != null) {
            a.a.a.a.b.m.a.a("AdvertiseManager", "onStartSuccess TxPowerLv=" + advertiseSettings.getTxPowerLevel() + " mode=" + advertiseSettings.getMode() + " timeout=" + advertiseSettings.getTimeout());
        } else {
            a.a.a.a.b.m.a.b("AdvertiseManager", "onStartSuccess, settingInEffect is null");
        }
        this.f1179a.onSuccess(true);
    }
}

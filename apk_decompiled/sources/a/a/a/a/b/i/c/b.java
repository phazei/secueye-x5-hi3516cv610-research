package a.a.a.a.b.i.c;

import aisble.callback.profile.ProfileDataCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;

/* JADX INFO: compiled from: ProvisionPlugin.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements ProfileDataCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ c f1382a;

    public b(c cVar) {
        this.f1382a = cVar;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(BluetoothDevice bluetoothDevice, Data data) {
        if (this.f1382a.h != null) {
            this.f1382a.h.onDataReceived(bluetoothDevice, data);
        }
    }

    @Override // aisble.callback.profile.ProfileDataCallback
    public void onInvalidDataReceived(BluetoothDevice bluetoothDevice, Data data) {
    }
}

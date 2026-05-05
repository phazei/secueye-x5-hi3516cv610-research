package a.a.a.a.b.b;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;

/* JADX INFO: compiled from: BleMeshManager.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1296a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1297b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ BleMeshManager f1298c;

    public c(BleMeshManager bleMeshManager, int i, byte[] bArr) {
        this.f1298c = bleMeshManager;
        this.f1296a = i;
        this.f1297b = bArr;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
        a.a.a.a.b.m.a.b(this.f1298c.TAG, "writeProvisionCharacteristic error: " + i);
        int i2 = this.f1296a;
        if (i2 <= 0) {
            ((BleMeshManagerCallbacks) this.f1298c.mCallbacks).onError(bluetoothDevice, BleMeshManager.ERROR_RETRY_WRITE_CHARACTERISTIC, i);
        } else {
            this.f1298c.send(this.f1297b, i2 - 1);
        }
    }
}

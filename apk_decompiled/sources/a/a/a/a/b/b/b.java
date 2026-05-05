package a.a.a.a.b.b;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;

/* JADX INFO: compiled from: BleMeshManager.java */
/* JADX INFO: loaded from: classes.dex */
public class b implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1293a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f1294b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ BleMeshManager f1295c;

    public b(BleMeshManager bleMeshManager, int i, byte[] bArr) {
        this.f1295c = bleMeshManager;
        this.f1293a = i;
        this.f1294b = bArr;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(BluetoothDevice bluetoothDevice, int i) {
        a.a.a.a.b.m.a.b(this.f1295c.TAG, "writeProxyCharacteristic error: " + i);
        int i2 = this.f1293a;
        if (i2 > 0) {
            this.f1295c.send(this.f1294b, i2 - 1);
        } else {
            if (this.f1295c.isProvisioningComplete) {
                return;
            }
            ((BleMeshManagerCallbacks) this.f1295c.mCallbacks).onError(bluetoothDevice, BleMeshManager.ERROR_RETRY_WRITE_CHARACTERISTIC, i);
        }
    }
}

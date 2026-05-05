package a.a.a.a.b.b;

import aisble.callback.FailCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;

/* JADX INFO: compiled from: BleMeshManager.java */
/* JADX INFO: loaded from: classes.dex */
public class d implements FailCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1299a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ BluetoothGattCharacteristic f1300b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ BleMeshManager f1301c;

    public d(BleMeshManager bleMeshManager, int i, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.f1301c = bleMeshManager;
        this.f1299a = i;
        this.f1300b = bluetoothGattCharacteristic;
    }

    @Override // aisble.callback.FailCallback
    public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
        int i2 = this.f1299a;
        if (i2 <= 1) {
            ((BleMeshManagerCallbacks) this.f1301c.mCallbacks).onError(bluetoothDevice, BleMeshManager.ERROR_RETRY_ENABLE_NOTIFICATION, i);
        } else {
            this.f1301c.internalEnableNotification(this.f1300b, i2 - 1);
        }
    }
}

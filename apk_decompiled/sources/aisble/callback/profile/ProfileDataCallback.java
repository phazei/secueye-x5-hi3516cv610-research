package aisble.callback.profile;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface ProfileDataCallback extends DataReceivedCallback {
    void onInvalidDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data);
}

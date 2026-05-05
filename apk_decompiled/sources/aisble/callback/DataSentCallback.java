package aisble.callback;

import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface DataSentCallback {
    void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data);
}

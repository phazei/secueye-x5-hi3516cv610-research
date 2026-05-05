package aisble.callback;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface RssiCallback {
    void onRssiRead(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = -128, to = 20) int i);
}

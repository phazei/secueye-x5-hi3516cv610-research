package aisble.callback;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface MtuCallback {
    void onMtuChanged(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = 23, to = 517) int i);
}

package aisble.callback;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public interface ReadProgressCallback {
    void onPacketReceived(@NonNull BluetoothDevice bluetoothDevice, @Nullable byte[] bArr, @IntRange(from = 0) int i);
}

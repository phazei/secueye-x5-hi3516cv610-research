package aisble.callback;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface PhyCallback {
    public static final int PHY_LE_1M = 1;
    public static final int PHY_LE_2M = 2;
    public static final int PHY_LE_CODED = 3;

    void onPhyChanged(@NonNull BluetoothDevice bluetoothDevice, int i, int i2);
}

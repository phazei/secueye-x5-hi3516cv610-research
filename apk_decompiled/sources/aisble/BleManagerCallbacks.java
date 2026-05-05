package aisble;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface BleManagerCallbacks {
    @Deprecated
    void onBatteryValueReceived(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = 0, to = 100) int i);

    void onBonded(@NonNull BluetoothDevice bluetoothDevice);

    void onBondingFailed(@NonNull BluetoothDevice bluetoothDevice);

    void onBondingRequired(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceConnected(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceConnecting(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceDisconnected(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceDisconnecting(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceNotSupported(@NonNull BluetoothDevice bluetoothDevice);

    void onDeviceReady(@NonNull BluetoothDevice bluetoothDevice);

    void onError(@NonNull BluetoothDevice bluetoothDevice, @NonNull String str, int i);

    void onLinkLossOccurred(@NonNull BluetoothDevice bluetoothDevice);

    void onServicesDiscovered(@NonNull BluetoothDevice bluetoothDevice, boolean z);

    @Deprecated
    boolean shouldEnableBatteryLevelNotifications(@NonNull BluetoothDevice bluetoothDevice);
}

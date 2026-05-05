package aisble.callback;

import android.bluetooth.BluetoothDevice;
import android.support.v4.media.MediaDescriptionCompat;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import view.CustomFinderView;

/* JADX INFO: loaded from: classes.dex */
public interface ConnectionPriorityCallback {
    void onConnectionUpdated(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200) int i, @IntRange(from = 0, to = 499) int i2, @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200) int i3);
}

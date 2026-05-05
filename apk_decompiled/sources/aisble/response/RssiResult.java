package aisble.response;

import aisble.callback.RssiCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class RssiResult implements RssiCallback, Parcelable {
    public static final Parcelable.Creator<RssiResult> CREATOR = new Parcelable.Creator<RssiResult>() { // from class: aisble.response.RssiResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RssiResult createFromParcel(Parcel parcel) {
            return new RssiResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RssiResult[] newArray(int i) {
            return new RssiResult[i];
        }
    };
    public BluetoothDevice device;

    @IntRange(from = -128, to = 20)
    public int rssi;

    public RssiResult(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    @IntRange(from = -128, to = 20)
    public int getRssi() {
        return this.rssi;
    }

    @Override // aisble.callback.RssiCallback
    public void onRssiRead(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = -128, to = 20) int i) {
        this.device = bluetoothDevice;
        this.rssi = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeInt(this.rssi);
    }
}

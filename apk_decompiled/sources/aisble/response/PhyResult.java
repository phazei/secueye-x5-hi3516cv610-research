package aisble.response;

import aisble.callback.PhyCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class PhyResult implements PhyCallback, Parcelable {
    public static final Parcelable.Creator<PhyResult> CREATOR = new Parcelable.Creator<PhyResult>() { // from class: aisble.response.PhyResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhyResult createFromParcel(Parcel parcel) {
            return new PhyResult(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PhyResult[] newArray(int i) {
            return new PhyResult[i];
        }
    };
    public BluetoothDevice device;
    public int rxPhy;
    public int txPhy;

    public PhyResult(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.txPhy = parcel.readInt();
        this.rxPhy = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    public int getRxPhy() {
        return this.rxPhy;
    }

    public int getTxPhy() {
        return this.txPhy;
    }

    @Override // aisble.callback.PhyCallback
    public void onPhyChanged(@NonNull BluetoothDevice bluetoothDevice, int i, int i2) {
        this.device = bluetoothDevice;
        this.txPhy = i;
        this.rxPhy = i2;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeInt(this.txPhy);
        parcel.writeInt(this.rxPhy);
    }
}

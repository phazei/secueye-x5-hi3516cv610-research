package aisble.response;

import aisble.callback.DataReceivedCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class ReadResponse implements DataReceivedCallback, Parcelable {
    public static final Parcelable.Creator<ReadResponse> CREATOR = new Parcelable.Creator<ReadResponse>() { // from class: aisble.response.ReadResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ReadResponse createFromParcel(Parcel parcel) {
            return new ReadResponse(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ReadResponse[] newArray(int i) {
            return new ReadResponse[i];
        }
    };
    public Data data;
    public BluetoothDevice device;

    public ReadResponse() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    @Nullable
    public Data getRawData() {
        return this.data;
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        this.device = bluetoothDevice;
        this.data = data;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeParcelable(this.data, i);
    }

    public ReadResponse(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.data = (Data) parcel.readParcelable(Data.class.getClassLoader());
    }
}

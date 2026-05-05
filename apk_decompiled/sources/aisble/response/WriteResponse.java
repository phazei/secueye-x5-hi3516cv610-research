package aisble.response;

import aisble.callback.DataSentCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class WriteResponse implements DataSentCallback, Parcelable {
    public static final Parcelable.Creator<WriteResponse> CREATOR = new Parcelable.Creator<WriteResponse>() { // from class: aisble.response.WriteResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WriteResponse createFromParcel(Parcel parcel) {
            return new WriteResponse(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WriteResponse[] newArray(int i) {
            return new WriteResponse[i];
        }
    };
    public Data data;
    public BluetoothDevice device;

    public WriteResponse(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.data = (Data) parcel.readParcelable(Data.class.getClassLoader());
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

    @Override // aisble.callback.DataSentCallback
    public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        this.device = bluetoothDevice;
        this.data = data;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeParcelable(this.data, i);
    }
}

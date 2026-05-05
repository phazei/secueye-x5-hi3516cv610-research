package aisble.response;

import aisble.callback.ConnectionPriorityCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaDescriptionCompat;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import view.CustomFinderView;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionPriorityResponse implements ConnectionPriorityCallback, Parcelable {
    public static final Parcelable.Creator<ConnectionPriorityResponse> CREATOR = new Parcelable.Creator<ConnectionPriorityResponse>() { // from class: aisble.response.ConnectionPriorityResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionPriorityResponse createFromParcel(Parcel parcel) {
            return new ConnectionPriorityResponse(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionPriorityResponse[] newArray(int i) {
            return new ConnectionPriorityResponse[i];
        }
    };
    public BluetoothDevice device;

    @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200)
    public int interval;

    @IntRange(from = 0, to = 499)
    public int latency;

    @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200)
    public int supervisionTimeout;

    public ConnectionPriorityResponse(Parcel parcel) {
        this.device = (BluetoothDevice) parcel.readParcelable(BluetoothDevice.class.getClassLoader());
        this.interval = parcel.readInt();
        this.latency = parcel.readInt();
        this.supervisionTimeout = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.device;
    }

    @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200)
    public int getConnectionInterval() {
        return this.interval;
    }

    @IntRange(from = 0, to = 499)
    public int getSlaveLatency() {
        return this.latency;
    }

    @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200)
    public int getSupervisionTimeout() {
        return this.supervisionTimeout;
    }

    @Override // aisble.callback.ConnectionPriorityCallback
    public void onConnectionUpdated(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200) int i, @IntRange(from = 0, to = 499) int i2, @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200) int i3) {
        this.device = bluetoothDevice;
        this.interval = i;
        this.latency = i2;
        this.supervisionTimeout = i3;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.device, i);
        parcel.writeInt(this.interval);
        parcel.writeInt(this.latency);
        parcel.writeInt(this.supervisionTimeout);
    }
}

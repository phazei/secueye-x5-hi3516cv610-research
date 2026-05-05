package aisble.callback.profile;

import aisble.data.Data;
import aisble.response.ReadResponse;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class ProfileReadResponse extends ReadResponse implements ProfileDataCallback, Parcelable {
    public static final Parcelable.Creator<ProfileReadResponse> CREATOR = new Parcelable.Creator<ProfileReadResponse>() { // from class: aisble.callback.profile.ProfileReadResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProfileReadResponse createFromParcel(Parcel parcel) {
            return new ProfileReadResponse(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProfileReadResponse[] newArray(int i) {
            return new ProfileReadResponse[i];
        }
    };
    public boolean valid;

    public ProfileReadResponse() {
        this.valid = true;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Override // aisble.callback.profile.ProfileDataCallback
    public void onInvalidDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        this.valid = false;
    }

    @Override // aisble.response.ReadResponse, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeByte(this.valid ? (byte) 1 : (byte) 0);
    }

    public ProfileReadResponse(Parcel parcel) {
        super(parcel);
        this.valid = true;
        this.valid = parcel.readByte() != 0;
    }
}

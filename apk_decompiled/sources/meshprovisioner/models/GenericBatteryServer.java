package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericBatteryServer extends SigModel {
    public static final Parcelable.Creator<GenericBatteryServer> CREATOR = new Parcelable.Creator<GenericBatteryServer>() { // from class: meshprovisioner.models.GenericBatteryServer.1
        @Override // android.os.Parcelable.Creator
        public GenericBatteryServer createFromParcel(Parcel parcel) {
            return new GenericBatteryServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericBatteryServer[] newArray(int i) {
            return new GenericBatteryServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Battery Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericBatteryServer() {
    }

    public GenericBatteryServer(int i) {
        super(i);
    }

    public GenericBatteryServer(Parcel parcel) {
        super(parcel);
    }
}

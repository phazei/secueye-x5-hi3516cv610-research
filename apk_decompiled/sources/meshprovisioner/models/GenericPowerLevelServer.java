package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericPowerLevelServer extends SigModel {
    public static final Parcelable.Creator<GenericPowerLevelServer> CREATOR = new Parcelable.Creator<GenericPowerLevelServer>() { // from class: meshprovisioner.models.GenericPowerLevelServer.1
        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelServer createFromParcel(Parcel parcel) {
            return new GenericPowerLevelServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelServer[] newArray(int i) {
            return new GenericPowerLevelServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Power Level Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericPowerLevelServer() {
    }

    public GenericPowerLevelServer(int i) {
        super(i);
    }

    public GenericPowerLevelServer(Parcel parcel) {
        super(parcel);
    }
}

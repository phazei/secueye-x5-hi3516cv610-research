package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericPowerLevelSetupServer extends SigModel {
    public static final Parcelable.Creator<GenericPowerLevelSetupServer> CREATOR = new Parcelable.Creator<GenericPowerLevelSetupServer>() { // from class: meshprovisioner.models.GenericPowerLevelSetupServer.1
        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelSetupServer createFromParcel(Parcel parcel) {
            return new GenericPowerLevelSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelSetupServer[] newArray(int i) {
            return new GenericPowerLevelSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Power Level Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericPowerLevelSetupServer() {
    }

    public GenericPowerLevelSetupServer(int i) {
        super(i);
    }

    public GenericPowerLevelSetupServer(Parcel parcel) {
        super(parcel);
    }
}

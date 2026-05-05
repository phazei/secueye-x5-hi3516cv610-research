package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericPowerOnOffSetupServer extends SigModel {
    public static final Parcelable.Creator<GenericPowerOnOffSetupServer> CREATOR = new Parcelable.Creator<GenericPowerOnOffSetupServer>() { // from class: meshprovisioner.models.GenericPowerOnOffSetupServer.1
        @Override // android.os.Parcelable.Creator
        public GenericPowerOnOffSetupServer createFromParcel(Parcel parcel) {
            return new GenericPowerOnOffSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericPowerOnOffSetupServer[] newArray(int i) {
            return new GenericPowerOnOffSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Power On Off Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericPowerOnOffSetupServer() {
    }

    public GenericPowerOnOffSetupServer(int i) {
        super(i);
    }

    public GenericPowerOnOffSetupServer(Parcel parcel) {
        super(parcel);
    }
}

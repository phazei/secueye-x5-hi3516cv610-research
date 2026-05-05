package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLocationSetupServer extends SigModel {
    public static final Parcelable.Creator<GenericLocationSetupServer> CREATOR = new Parcelable.Creator<GenericLocationSetupServer>() { // from class: meshprovisioner.models.GenericLocationSetupServer.1
        @Override // android.os.Parcelable.Creator
        public GenericLocationSetupServer createFromParcel(Parcel parcel) {
            return new GenericLocationSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericLocationSetupServer[] newArray(int i) {
            return new GenericLocationSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Location Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericLocationSetupServer() {
    }

    public GenericLocationSetupServer(int i) {
        super(i);
    }

    public GenericLocationSetupServer(Parcel parcel) {
        super(parcel);
    }
}

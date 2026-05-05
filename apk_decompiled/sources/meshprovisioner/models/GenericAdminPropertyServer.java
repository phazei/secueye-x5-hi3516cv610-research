package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericAdminPropertyServer extends SigModel {
    public static final Parcelable.Creator<GenericAdminPropertyServer> CREATOR = new Parcelable.Creator<GenericAdminPropertyServer>() { // from class: meshprovisioner.models.GenericAdminPropertyServer.1
        @Override // android.os.Parcelable.Creator
        public GenericAdminPropertyServer createFromParcel(Parcel parcel) {
            return new GenericAdminPropertyServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericAdminPropertyServer[] newArray(int i) {
            return new GenericAdminPropertyServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Admin Property Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericAdminPropertyServer() {
    }

    public GenericAdminPropertyServer(int i) {
        super(i);
    }

    public GenericAdminPropertyServer(Parcel parcel) {
        super(parcel);
    }
}

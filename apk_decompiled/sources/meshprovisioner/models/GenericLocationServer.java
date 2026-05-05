package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLocationServer extends SigModel {
    public static final Parcelable.Creator<GenericLocationServer> CREATOR = new Parcelable.Creator<GenericLocationServer>() { // from class: meshprovisioner.models.GenericLocationServer.1
        @Override // android.os.Parcelable.Creator
        public GenericLocationServer createFromParcel(Parcel parcel) {
            return new GenericLocationServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericLocationServer[] newArray(int i) {
            return new GenericLocationServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Location Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericLocationServer() {
    }

    public GenericLocationServer(int i) {
        super(i);
    }

    public GenericLocationServer(Parcel parcel) {
        super(parcel);
    }
}

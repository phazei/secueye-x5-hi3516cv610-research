package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericManufacturerPropertyServer extends SigModel {
    public static final Parcelable.Creator<GenericManufacturerPropertyServer> CREATOR = new Parcelable.Creator<GenericManufacturerPropertyServer>() { // from class: meshprovisioner.models.GenericManufacturerPropertyServer.1
        @Override // android.os.Parcelable.Creator
        public GenericManufacturerPropertyServer createFromParcel(Parcel parcel) {
            return new GenericManufacturerPropertyServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericManufacturerPropertyServer[] newArray(int i) {
            return new GenericManufacturerPropertyServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Manufacturer Property Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericManufacturerPropertyServer() {
    }

    public GenericManufacturerPropertyServer(int i) {
        super(i);
    }

    public GenericManufacturerPropertyServer(Parcel parcel) {
        super(parcel);
    }
}

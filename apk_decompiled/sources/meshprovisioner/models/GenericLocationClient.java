package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLocationClient extends SigModel {
    public static final Parcelable.Creator<GenericLocationClient> CREATOR = new Parcelable.Creator<GenericLocationClient>() { // from class: meshprovisioner.models.GenericLocationClient.1
        @Override // android.os.Parcelable.Creator
        public GenericLocationClient createFromParcel(Parcel parcel) {
            return new GenericLocationClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericLocationClient[] newArray(int i) {
            return new GenericLocationClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Location Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericLocationClient() {
    }

    public GenericLocationClient(int i) {
        super(i);
    }

    public GenericLocationClient(Parcel parcel) {
        super(parcel);
    }
}

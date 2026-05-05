package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericPowerLevelClient extends SigModel {
    public static final Parcelable.Creator<GenericPowerLevelClient> CREATOR = new Parcelable.Creator<GenericPowerLevelClient>() { // from class: meshprovisioner.models.GenericPowerLevelClient.1
        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelClient createFromParcel(Parcel parcel) {
            return new GenericPowerLevelClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericPowerLevelClient[] newArray(int i) {
            return new GenericPowerLevelClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Power Level Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericPowerLevelClient() {
    }

    public GenericPowerLevelClient(int i) {
        super(i);
    }

    public GenericPowerLevelClient(Parcel parcel) {
        super(parcel);
    }
}

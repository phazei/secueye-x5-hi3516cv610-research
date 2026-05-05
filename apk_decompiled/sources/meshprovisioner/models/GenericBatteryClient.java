package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericBatteryClient extends SigModel {
    public static final Parcelable.Creator<GenericBatteryClient> CREATOR = new Parcelable.Creator<GenericBatteryClient>() { // from class: meshprovisioner.models.GenericBatteryClient.1
        @Override // android.os.Parcelable.Creator
        public GenericBatteryClient createFromParcel(Parcel parcel) {
            return new GenericBatteryClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericBatteryClient[] newArray(int i) {
            return new GenericBatteryClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Battery Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericBatteryClient() {
    }

    public GenericBatteryClient(int i) {
        super(i);
    }

    public GenericBatteryClient(Parcel parcel) {
        super(parcel);
    }
}

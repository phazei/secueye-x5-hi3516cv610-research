package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightHslClient extends SigModel {
    public static final Parcelable.Creator<LightHslClient> CREATOR = new Parcelable.Creator<LightHslClient>() { // from class: meshprovisioner.models.LightHslClient.1
        @Override // android.os.Parcelable.Creator
        public LightHslClient createFromParcel(Parcel parcel) {
            return new LightHslClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightHslClient[] newArray(int i) {
            return new LightHslClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light HSL Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightHslClient() {
    }

    public LightHslClient(int i) {
        super(i);
    }

    public LightHslClient(Parcel parcel) {
        super(parcel);
    }
}

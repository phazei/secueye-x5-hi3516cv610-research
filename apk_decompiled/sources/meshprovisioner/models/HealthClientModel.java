package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class HealthClientModel extends SigModel {
    public static final Parcelable.Creator<HealthClientModel> CREATOR = new Parcelable.Creator<HealthClientModel>() { // from class: meshprovisioner.models.HealthClientModel.1
        @Override // android.os.Parcelable.Creator
        public HealthClientModel createFromParcel(Parcel parcel) {
            return new HealthClientModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public HealthClientModel[] newArray(int i) {
            return new HealthClientModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Health Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public HealthClientModel() {
    }

    public HealthClientModel(int i) {
        super(i);
    }

    public HealthClientModel(Parcel parcel) {
        super(parcel);
    }
}

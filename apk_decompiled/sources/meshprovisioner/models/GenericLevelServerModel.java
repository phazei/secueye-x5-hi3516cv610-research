package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLevelServerModel extends SigModel {
    public static final Parcelable.Creator<GenericLevelServerModel> CREATOR = new Parcelable.Creator<GenericLevelServerModel>() { // from class: meshprovisioner.models.GenericLevelServerModel.1
        @Override // android.os.Parcelable.Creator
        public GenericLevelServerModel createFromParcel(Parcel parcel) {
            return new GenericLevelServerModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericLevelServerModel[] newArray(int i) {
            return new GenericLevelServerModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Level Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericLevelServerModel() {
    }

    public GenericLevelServerModel(int i) {
        super(i);
    }

    public GenericLevelServerModel(Parcel parcel) {
        super(parcel);
    }
}

package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericLevelClientModel extends SigModel {
    public static final Parcelable.Creator<GenericLevelClientModel> CREATOR = new Parcelable.Creator<GenericLevelClientModel>() { // from class: meshprovisioner.models.GenericLevelClientModel.1
        @Override // android.os.Parcelable.Creator
        public GenericLevelClientModel createFromParcel(Parcel parcel) {
            return new GenericLevelClientModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericLevelClientModel[] newArray(int i) {
            return new GenericLevelClientModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Level Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericLevelClientModel() {
    }

    public GenericLevelClientModel(int i) {
        super(i);
    }

    public GenericLevelClientModel(Parcel parcel) {
        super(parcel);
    }
}

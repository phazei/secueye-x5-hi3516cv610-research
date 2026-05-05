package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericOnOffServerModel extends SigModel {
    public static final Parcelable.Creator<GenericOnOffServerModel> CREATOR = new Parcelable.Creator<GenericOnOffServerModel>() { // from class: meshprovisioner.models.GenericOnOffServerModel.1
        @Override // android.os.Parcelable.Creator
        public GenericOnOffServerModel createFromParcel(Parcel parcel) {
            return new GenericOnOffServerModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericOnOffServerModel[] newArray(int i) {
            return new GenericOnOffServerModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic On Off Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericOnOffServerModel() {
    }

    public GenericOnOffServerModel(int i) {
        super(i);
    }

    public GenericOnOffServerModel(Parcel parcel) {
        super(parcel);
    }
}

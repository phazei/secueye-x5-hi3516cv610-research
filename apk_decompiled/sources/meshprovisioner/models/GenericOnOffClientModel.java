package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericOnOffClientModel extends SigModel {
    public static final Parcelable.Creator<GenericOnOffClientModel> CREATOR = new Parcelable.Creator<GenericOnOffClientModel>() { // from class: meshprovisioner.models.GenericOnOffClientModel.1
        @Override // android.os.Parcelable.Creator
        public GenericOnOffClientModel createFromParcel(Parcel parcel) {
            return new GenericOnOffClientModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericOnOffClientModel[] newArray(int i) {
            return new GenericOnOffClientModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic On Off Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericOnOffClientModel() {
    }

    public GenericOnOffClientModel(Parcel parcel) {
        super(parcel);
    }

    public GenericOnOffClientModel(int i) {
        super(i);
    }
}

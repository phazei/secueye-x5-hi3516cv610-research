package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigurationClientModel extends SigModel {
    public static final Parcelable.Creator<ConfigurationClientModel> CREATOR = new Parcelable.Creator<ConfigurationClientModel>() { // from class: meshprovisioner.models.ConfigurationClientModel.1
        @Override // android.os.Parcelable.Creator
        public ConfigurationClientModel createFromParcel(Parcel parcel) {
            return new ConfigurationClientModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ConfigurationClientModel[] newArray(int i) {
            return new ConfigurationClientModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Configuration Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public ConfigurationClientModel() {
    }

    public ConfigurationClientModel(int i) {
        super(i);
    }

    public ConfigurationClientModel(Parcel parcel) {
        super(parcel);
    }
}

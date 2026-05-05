package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigurationServerModel extends SigModel {
    public static final Parcelable.Creator<ConfigurationServerModel> CREATOR = new Parcelable.Creator<ConfigurationServerModel>() { // from class: meshprovisioner.models.ConfigurationServerModel.1
        @Override // android.os.Parcelable.Creator
        public ConfigurationServerModel createFromParcel(Parcel parcel) {
            return new ConfigurationServerModel(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public ConfigurationServerModel[] newArray(int i) {
            return new ConfigurationServerModel[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Configuration Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public ConfigurationServerModel() {
    }

    public ConfigurationServerModel(int i) {
        super(i);
    }

    public ConfigurationServerModel(Parcel parcel) {
        super(parcel);
    }
}

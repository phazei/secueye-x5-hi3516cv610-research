package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightLcSetupServer extends SigModel {
    public static final Parcelable.Creator<LightLcSetupServer> CREATOR = new Parcelable.Creator<LightLcSetupServer>() { // from class: meshprovisioner.models.LightLcSetupServer.1
        @Override // android.os.Parcelable.Creator
        public LightLcSetupServer createFromParcel(Parcel parcel) {
            return new LightLcSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightLcSetupServer[] newArray(int i) {
            return new LightLcSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light LC Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightLcSetupServer() {
    }

    public LightLcSetupServer(int i) {
        super(i);
    }

    public LightLcSetupServer(Parcel parcel) {
        super(parcel);
    }
}

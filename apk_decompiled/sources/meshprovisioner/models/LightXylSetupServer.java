package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightXylSetupServer extends SigModel {
    public static final Parcelable.Creator<LightXylSetupServer> CREATOR = new Parcelable.Creator<LightXylSetupServer>() { // from class: meshprovisioner.models.LightXylSetupServer.1
        @Override // android.os.Parcelable.Creator
        public LightXylSetupServer createFromParcel(Parcel parcel) {
            return new LightXylSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightXylSetupServer[] newArray(int i) {
            return new LightXylSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light XYL Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightXylSetupServer() {
    }

    public LightXylSetupServer(int i) {
        super(i);
    }

    public LightXylSetupServer(Parcel parcel) {
        super(parcel);
    }
}

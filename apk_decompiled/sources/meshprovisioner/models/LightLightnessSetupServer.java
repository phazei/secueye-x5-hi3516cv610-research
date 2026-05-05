package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightLightnessSetupServer extends SigModel {
    public static final Parcelable.Creator<LightLightnessSetupServer> CREATOR = new Parcelable.Creator<LightLightnessSetupServer>() { // from class: meshprovisioner.models.LightLightnessSetupServer.1
        @Override // android.os.Parcelable.Creator
        public LightLightnessSetupServer createFromParcel(Parcel parcel) {
            return new LightLightnessSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightLightnessSetupServer[] newArray(int i) {
            return new LightLightnessSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light Lightness Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightLightnessSetupServer() {
    }

    public LightLightnessSetupServer(int i) {
        super(i);
    }

    public LightLightnessSetupServer(Parcel parcel) {
        super(parcel);
    }
}

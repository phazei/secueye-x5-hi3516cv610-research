package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightLightnessServer extends SigModel {
    public static final Parcelable.Creator<LightLightnessServer> CREATOR = new Parcelable.Creator<LightLightnessServer>() { // from class: meshprovisioner.models.LightLightnessServer.1
        @Override // android.os.Parcelable.Creator
        public LightLightnessServer createFromParcel(Parcel parcel) {
            return new LightLightnessServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightLightnessServer[] newArray(int i) {
            return new LightLightnessServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light Lightness Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightLightnessServer() {
    }

    public LightLightnessServer(int i) {
        super(i);
    }

    public LightLightnessServer(Parcel parcel) {
        super(parcel);
    }
}

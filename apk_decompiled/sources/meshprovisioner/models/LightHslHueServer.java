package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightHslHueServer extends SigModel {
    public static final Parcelable.Creator<LightHslHueServer> CREATOR = new Parcelable.Creator<LightHslHueServer>() { // from class: meshprovisioner.models.LightHslHueServer.1
        @Override // android.os.Parcelable.Creator
        public LightHslHueServer createFromParcel(Parcel parcel) {
            return new LightHslHueServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightHslHueServer[] newArray(int i) {
            return new LightHslHueServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light HSL Hue Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightHslHueServer() {
    }

    public LightHslHueServer(int i) {
        super(i);
    }

    public LightHslHueServer(Parcel parcel) {
        super(parcel);
    }
}

package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightHslSaturationServer extends SigModel {
    public static final Parcelable.Creator<LightHslSaturationServer> CREATOR = new Parcelable.Creator<LightHslSaturationServer>() { // from class: meshprovisioner.models.LightHslSaturationServer.1
        @Override // android.os.Parcelable.Creator
        public LightHslSaturationServer createFromParcel(Parcel parcel) {
            return new LightHslSaturationServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightHslSaturationServer[] newArray(int i) {
            return new LightHslSaturationServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light HSL Saturation Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightHslSaturationServer() {
    }

    public LightHslSaturationServer(int i) {
        super(i);
    }

    public LightHslSaturationServer(Parcel parcel) {
        super(parcel);
    }
}

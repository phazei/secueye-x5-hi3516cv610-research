package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightCtlTemperatureServer extends SigModel {
    public static final Parcelable.Creator<LightCtlTemperatureServer> CREATOR = new Parcelable.Creator<LightCtlTemperatureServer>() { // from class: meshprovisioner.models.LightCtlTemperatureServer.1
        @Override // android.os.Parcelable.Creator
        public LightCtlTemperatureServer createFromParcel(Parcel parcel) {
            return new LightCtlTemperatureServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightCtlTemperatureServer[] newArray(int i) {
            return new LightCtlTemperatureServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light Ctl Temperature Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightCtlTemperatureServer() {
    }

    public LightCtlTemperatureServer(int i) {
        super(i);
    }

    public LightCtlTemperatureServer(Parcel parcel) {
        super(parcel);
    }
}

package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SensorSetupServer extends SigModel {
    public static final Parcelable.Creator<SensorSetupServer> CREATOR = new Parcelable.Creator<SensorSetupServer>() { // from class: meshprovisioner.models.SensorSetupServer.1
        @Override // android.os.Parcelable.Creator
        public SensorSetupServer createFromParcel(Parcel parcel) {
            return new SensorSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SensorSetupServer[] newArray(int i) {
            return new SensorSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Sensor Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SensorSetupServer() {
    }

    public SensorSetupServer(int i) {
        super(i);
    }

    public SensorSetupServer(Parcel parcel) {
        super(parcel);
    }
}

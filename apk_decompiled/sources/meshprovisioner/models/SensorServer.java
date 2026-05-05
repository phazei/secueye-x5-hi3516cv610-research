package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SensorServer extends SigModel {
    public static final Parcelable.Creator<SensorServer> CREATOR = new Parcelable.Creator<SensorServer>() { // from class: meshprovisioner.models.SensorServer.1
        @Override // android.os.Parcelable.Creator
        public SensorServer createFromParcel(Parcel parcel) {
            return new SensorServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SensorServer[] newArray(int i) {
            return new SensorServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Sensor Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SensorServer() {
    }

    public SensorServer(int i) {
        super(i);
    }

    public SensorServer(Parcel parcel) {
        super(parcel);
    }
}

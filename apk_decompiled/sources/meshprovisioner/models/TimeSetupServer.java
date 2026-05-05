package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class TimeSetupServer extends SigModel {
    public static final Parcelable.Creator<TimeSetupServer> CREATOR = new Parcelable.Creator<TimeSetupServer>() { // from class: meshprovisioner.models.TimeSetupServer.1
        @Override // android.os.Parcelable.Creator
        public TimeSetupServer createFromParcel(Parcel parcel) {
            return new TimeSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TimeSetupServer[] newArray(int i) {
            return new TimeSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Time Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public TimeSetupServer() {
    }

    public TimeSetupServer(int i) {
        super(i);
    }

    public TimeSetupServer(Parcel parcel) {
        super(parcel);
    }
}

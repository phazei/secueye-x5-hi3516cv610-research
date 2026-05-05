package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class TimeServer extends SigModel {
    public static final Parcelable.Creator<TimeServer> CREATOR = new Parcelable.Creator<TimeServer>() { // from class: meshprovisioner.models.TimeServer.1
        @Override // android.os.Parcelable.Creator
        public TimeServer createFromParcel(Parcel parcel) {
            return new TimeServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TimeServer[] newArray(int i) {
            return new TimeServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Time Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public TimeServer() {
    }

    public TimeServer(int i) {
        super(i);
    }

    public TimeServer(Parcel parcel) {
        super(parcel);
    }
}

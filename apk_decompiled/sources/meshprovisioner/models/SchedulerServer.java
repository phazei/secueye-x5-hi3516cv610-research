package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SchedulerServer extends SigModel {
    public static final Parcelable.Creator<SchedulerServer> CREATOR = new Parcelable.Creator<SchedulerServer>() { // from class: meshprovisioner.models.SchedulerServer.1
        @Override // android.os.Parcelable.Creator
        public SchedulerServer createFromParcel(Parcel parcel) {
            return new SchedulerServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SchedulerServer[] newArray(int i) {
            return new SchedulerServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Scheduler Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SchedulerServer() {
    }

    public SchedulerServer(int i) {
        super(i);
    }

    public SchedulerServer(Parcel parcel) {
        super(parcel);
    }
}

package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SchedulerSetupServer extends SigModel {
    public static final Parcelable.Creator<SchedulerSetupServer> CREATOR = new Parcelable.Creator<SchedulerSetupServer>() { // from class: meshprovisioner.models.SchedulerSetupServer.1
        @Override // android.os.Parcelable.Creator
        public SchedulerSetupServer createFromParcel(Parcel parcel) {
            return new SchedulerSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SchedulerSetupServer[] newArray(int i) {
            return new SchedulerSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Scheduler Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SchedulerSetupServer() {
    }

    public SchedulerSetupServer(int i) {
        super(i);
    }

    public SchedulerSetupServer(Parcel parcel) {
        super(parcel);
    }
}

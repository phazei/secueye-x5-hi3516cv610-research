package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SchedulerClient extends SigModel {
    public static final Parcelable.Creator<SchedulerClient> CREATOR = new Parcelable.Creator<SchedulerClient>() { // from class: meshprovisioner.models.SchedulerClient.1
        @Override // android.os.Parcelable.Creator
        public SchedulerClient createFromParcel(Parcel parcel) {
            return new SchedulerClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SchedulerClient[] newArray(int i) {
            return new SchedulerClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Scheduler Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SchedulerClient() {
    }

    public SchedulerClient(int i) {
        super(i);
    }

    public SchedulerClient(Parcel parcel) {
        super(parcel);
    }
}

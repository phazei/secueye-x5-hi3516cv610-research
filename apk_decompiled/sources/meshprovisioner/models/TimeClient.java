package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class TimeClient extends SigModel {
    public static final Parcelable.Creator<TimeClient> CREATOR = new Parcelable.Creator<TimeClient>() { // from class: meshprovisioner.models.TimeClient.1
        @Override // android.os.Parcelable.Creator
        public TimeClient createFromParcel(Parcel parcel) {
            return new TimeClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TimeClient[] newArray(int i) {
            return new TimeClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Time Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public TimeClient() {
    }

    public TimeClient(int i) {
        super(i);
    }

    public TimeClient(Parcel parcel) {
        super(parcel);
    }
}

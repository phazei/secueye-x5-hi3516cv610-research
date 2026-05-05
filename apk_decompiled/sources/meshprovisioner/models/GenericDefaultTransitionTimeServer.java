package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericDefaultTransitionTimeServer extends SigModel {
    public static final Parcelable.Creator<GenericDefaultTransitionTimeServer> CREATOR = new Parcelable.Creator<GenericDefaultTransitionTimeServer>() { // from class: meshprovisioner.models.GenericDefaultTransitionTimeServer.1
        @Override // android.os.Parcelable.Creator
        public GenericDefaultTransitionTimeServer createFromParcel(Parcel parcel) {
            return new GenericDefaultTransitionTimeServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericDefaultTransitionTimeServer[] newArray(int i) {
            return new GenericDefaultTransitionTimeServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Default Transition Timer Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericDefaultTransitionTimeServer() {
    }

    public GenericDefaultTransitionTimeServer(int i) {
        super(i);
    }

    public GenericDefaultTransitionTimeServer(Parcel parcel) {
        super(parcel);
    }
}

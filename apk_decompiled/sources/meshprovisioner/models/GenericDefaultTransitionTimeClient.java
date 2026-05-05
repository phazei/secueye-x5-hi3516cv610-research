package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericDefaultTransitionTimeClient extends SigModel {
    public static final Parcelable.Creator<GenericDefaultTransitionTimeClient> CREATOR = new Parcelable.Creator<GenericDefaultTransitionTimeClient>() { // from class: meshprovisioner.models.GenericDefaultTransitionTimeClient.1
        @Override // android.os.Parcelable.Creator
        public GenericDefaultTransitionTimeClient createFromParcel(Parcel parcel) {
            return new GenericDefaultTransitionTimeClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericDefaultTransitionTimeClient[] newArray(int i) {
            return new GenericDefaultTransitionTimeClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic Default Transition Timer Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericDefaultTransitionTimeClient() {
    }

    public GenericDefaultTransitionTimeClient(int i) {
        super(i);
    }

    public GenericDefaultTransitionTimeClient(Parcel parcel) {
        super(parcel);
    }
}

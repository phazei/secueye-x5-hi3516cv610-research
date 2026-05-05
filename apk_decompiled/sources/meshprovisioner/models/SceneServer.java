package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SceneServer extends SigModel {
    public static final Parcelable.Creator<SceneServer> CREATOR = new Parcelable.Creator<SceneServer>() { // from class: meshprovisioner.models.SceneServer.1
        @Override // android.os.Parcelable.Creator
        public SceneServer createFromParcel(Parcel parcel) {
            return new SceneServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SceneServer[] newArray(int i) {
            return new SceneServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Scene Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SceneServer() {
    }

    public SceneServer(int i) {
        super(i);
    }

    public SceneServer(Parcel parcel) {
        super(parcel);
    }
}

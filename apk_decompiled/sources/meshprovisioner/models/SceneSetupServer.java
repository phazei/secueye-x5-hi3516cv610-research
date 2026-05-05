package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class SceneSetupServer extends SigModel {
    public static final Parcelable.Creator<SceneSetupServer> CREATOR = new Parcelable.Creator<SceneSetupServer>() { // from class: meshprovisioner.models.SceneSetupServer.1
        @Override // android.os.Parcelable.Creator
        public SceneSetupServer createFromParcel(Parcel parcel) {
            return new SceneSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SceneSetupServer[] newArray(int i) {
            return new SceneSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Scene Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public SceneSetupServer() {
    }

    public SceneSetupServer(int i) {
        super(i);
    }

    public SceneSetupServer(Parcel parcel) {
        super(parcel);
    }
}

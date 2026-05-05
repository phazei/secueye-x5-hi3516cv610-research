package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightCtlSetupServer extends SigModel {
    public static final Parcelable.Creator<LightCtlSetupServer> CREATOR = new Parcelable.Creator<LightCtlSetupServer>() { // from class: meshprovisioner.models.LightCtlSetupServer.1
        @Override // android.os.Parcelable.Creator
        public LightCtlSetupServer createFromParcel(Parcel parcel) {
            return new LightCtlSetupServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightCtlSetupServer[] newArray(int i) {
            return new LightCtlSetupServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light Ctl Setup Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightCtlSetupServer() {
    }

    public LightCtlSetupServer(int i) {
        super(i);
    }

    public LightCtlSetupServer(Parcel parcel) {
        super(parcel);
    }
}

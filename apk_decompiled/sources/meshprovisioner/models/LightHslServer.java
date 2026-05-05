package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightHslServer extends SigModel {
    public static final Parcelable.Creator<LightHslServer> CREATOR = new Parcelable.Creator<LightHslServer>() { // from class: meshprovisioner.models.LightHslServer.1
        @Override // android.os.Parcelable.Creator
        public LightHslServer createFromParcel(Parcel parcel) {
            return new LightHslServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightHslServer[] newArray(int i) {
            return new LightHslServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light HSL Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightHslServer() {
    }

    public LightHslServer(int i) {
        super(i);
    }

    public LightHslServer(Parcel parcel) {
        super(parcel);
    }
}

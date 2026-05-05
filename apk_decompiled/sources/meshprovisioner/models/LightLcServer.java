package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightLcServer extends SigModel {
    public static final Parcelable.Creator<LightLcServer> CREATOR = new Parcelable.Creator<LightLcServer>() { // from class: meshprovisioner.models.LightLcServer.1
        @Override // android.os.Parcelable.Creator
        public LightLcServer createFromParcel(Parcel parcel) {
            return new LightLcServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightLcServer[] newArray(int i) {
            return new LightLcServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light LC Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightLcServer() {
    }

    public LightLcServer(int i) {
        super(i);
    }

    public LightLcServer(Parcel parcel) {
        super(parcel);
    }
}

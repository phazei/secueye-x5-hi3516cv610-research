package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class LightCtlClient extends SigModel {
    public static final Parcelable.Creator<LightCtlClient> CREATOR = new Parcelable.Creator<LightCtlClient>() { // from class: meshprovisioner.models.LightCtlClient.1
        @Override // android.os.Parcelable.Creator
        public LightCtlClient createFromParcel(Parcel parcel) {
            return new LightCtlClient(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public LightCtlClient[] newArray(int i) {
            return new LightCtlClient[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Light Ctl Client";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public LightCtlClient() {
    }

    public LightCtlClient(int i) {
        super(i);
    }

    public LightCtlClient(Parcel parcel) {
        super(parcel);
    }
}

package meshprovisioner.models;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes4.dex */
public class GenericUserPropertyServer extends SigModel {
    public static final Parcelable.Creator<GenericUserPropertyServer> CREATOR = new Parcelable.Creator<GenericUserPropertyServer>() { // from class: meshprovisioner.models.GenericUserPropertyServer.1
        @Override // android.os.Parcelable.Creator
        public GenericUserPropertyServer createFromParcel(Parcel parcel) {
            return new GenericUserPropertyServer(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public GenericUserPropertyServer[] newArray(int i) {
            return new GenericUserPropertyServer[i];
        }
    };

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // meshprovisioner.configuration.MeshModel
    public String getModelName() {
        return "Generic User Property Server";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.parcelMeshModel(parcel, i);
    }

    public GenericUserPropertyServer() {
    }

    public GenericUserPropertyServer(int i) {
        super(i);
    }

    public GenericUserPropertyServer(Parcel parcel) {
        super(parcel);
    }
}

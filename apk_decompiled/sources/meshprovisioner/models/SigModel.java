package meshprovisioner.models;

import android.os.Parcel;
import meshprovisioner.configuration.MeshModel;

/* JADX INFO: loaded from: classes4.dex */
public abstract class SigModel extends MeshModel {
    public static final int DEFAULT_MODEL_ID = -1;
    public static final int MODEL_ID_LENGTH = 2;

    public SigModel() {
        super(-1);
    }

    @Override // meshprovisioner.configuration.MeshModel
    public int getModelId() {
        return this.mModelId;
    }

    public SigModel(int i) {
        super(i);
    }

    public SigModel(Parcel parcel) {
        super(parcel);
    }
}

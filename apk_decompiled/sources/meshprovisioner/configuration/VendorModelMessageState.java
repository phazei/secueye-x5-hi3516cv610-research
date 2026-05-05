package meshprovisioner.configuration;

import android.content.Context;
import b.InterfaceC0369c;

/* JADX INFO: loaded from: classes4.dex */
public abstract class VendorModelMessageState extends MeshMessageState {
    public static final String TAG = "VendorModelMessageState";

    public VendorModelMessageState(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public void executeResend() {
        super.executeResend();
    }
}

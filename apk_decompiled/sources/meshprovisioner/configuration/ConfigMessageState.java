package meshprovisioner.configuration;

import android.content.Context;
import b.InterfaceC0369c;
import meshprovisioner.configuration.MeshMessageState;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ConfigMessageState extends MeshMessageState {
    public static final String TAG = "ConfigMessageState";

    public ConfigMessageState(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public void executeResend() {
        super.executeResend();
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public abstract MeshMessageState.MessageState getState();
}

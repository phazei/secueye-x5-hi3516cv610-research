package com.alibaba.ailabs.iot.mesh.provision.callback;

import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;

/* JADX INFO: loaded from: classes.dex */
public interface FastProvisionV2StatusCallback {
    void onProvisioningComplete(ProvisionedMeshNode provisionedMeshNode);

    void onProvisioningDataSent(UnprovisionedMeshNode unprovisionedMeshNode);

    void onProvisioningFailed(BaseMeshNode baseMeshNode, int i, String str);
}

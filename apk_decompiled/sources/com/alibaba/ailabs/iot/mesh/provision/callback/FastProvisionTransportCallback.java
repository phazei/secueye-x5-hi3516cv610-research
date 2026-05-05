package com.alibaba.ailabs.iot.mesh.provision.callback;

import meshprovisioner.BaseMeshNode;

/* JADX INFO: loaded from: classes.dex */
public interface FastProvisionTransportCallback {
    void onFastProvisionDataSend(BaseMeshNode baseMeshNode, byte[] bArr);

    void onReceiveFastProvisionData(BaseMeshNode baseMeshNode, byte[] bArr);
}

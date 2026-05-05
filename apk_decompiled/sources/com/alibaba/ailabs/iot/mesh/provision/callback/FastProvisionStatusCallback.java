package com.alibaba.ailabs.iot.mesh.provision.callback;

import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;

/* JADX INFO: loaded from: classes.dex */
public interface FastProvisionStatusCallback {
    void onAddAppKeyMsgRespReceived(ProvisionedMeshNode provisionedMeshNode);

    void onAddAppKeyMsgSend(ProvisionedMeshNode provisionedMeshNode);

    void onBroadcastingRandoms(UnprovisionedMeshNodeData unprovisionedMeshNodeData);

    void onConfigInfoReceived(ProvisionedMeshNode provisionedMeshNode);

    void onProvisionFailed(int i, String str);

    void onReceiveConfirmationFromCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData, String str);

    void onReceiveDeviceConfirmationFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr);

    void onReceiveProvisionInfoRspFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr);

    void onReceiveSendProvisionDataRspFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr);

    void onReceiveVerifyResultFromCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData);

    void onSendProvisionConfigInfo(UnprovisionedMeshNodeData unprovisionedMeshNodeData);

    void onSendProvisionDataToDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData);

    void onSendRandomAndDeviceConfirmationToCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData);

    void onSendRandomToCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData);
}

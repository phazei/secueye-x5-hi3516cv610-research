package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.MeshStatusCallback;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy;

/* JADX INFO: compiled from: ConcurrentAppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class G implements MeshStatusCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy f3663a;

    public G(ConcurrentAppMeshStrategy concurrentAppMeshStrategy) {
        this.f3663a = concurrentAppMeshStrategy;
    }

    @Override // com.alibaba.ailabs.iot.mesh.StatusCallback
    public void onStatus(int i, String str) {
        DeviceInfo deviceInfoBuildDeviceInfoViaMac;
        ALog.d(ConcurrentAppMeshStrategy.TAG, "mesh sdk onStatus() called with: statusCode = [" + i + "], statusMsg = [" + str + "]");
        this.f3663a.meshProvisionStatus = i;
        this.f3663a.meshProvisionErrorMessage = str;
        if (this.f3663a.provisionHasStopped.get()) {
            ALog.d(ConcurrentAppMeshStrategy.TAG, "provisionHasStopped = true, return.");
            return;
        }
        if (i == 1) {
            ALog.d(ConcurrentAppMeshStrategy.TAG, "mesh sdk init success.");
            ConcurrentAppMeshStrategy concurrentAppMeshStrategy = this.f3663a;
            concurrentAppMeshStrategy.startMeshDeviceProvision(concurrentAppMeshStrategy.mSerialExecuteIndex = 0);
        }
        if (i == -1) {
            if (this.f3663a.unprovisionedBluetoothMeshDeviceList == null || this.f3663a.mSerialExecuteIndex >= this.f3663a.unprovisionedBluetoothMeshDeviceList.size()) {
                deviceInfoBuildDeviceInfoViaMac = null;
            } else {
                deviceInfoBuildDeviceInfoViaMac = this.f3663a.buildDeviceInfoViaMac(((UnprovisionedBluetoothMeshDevice) this.f3663a.unprovisionedBluetoothMeshDeviceList.get(this.f3663a.mSerialExecuteIndex)).getAddress());
            }
            this.f3663a.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk returned bind fail, state=" + i + ", msg=" + str).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_INIT_EXCEPTION);
            if (deviceInfoBuildDeviceInfoViaMac != null) {
                this.f3663a.provisionErrorInfo.setExtra(deviceInfoBuildDeviceInfoViaMac);
            }
            this.f3663a.provisionResultCallback(null);
            this.f3663a.scheduleNextConfigTask();
            return;
        }
        if (i != -3) {
            if (i == 20) {
                String string = JSON.parseObject(str).getString(AlinkConstants.KEY_MAC);
                ProvisionStatus provisionStatus = ProvisionStatus.PROVISION_START_IN_CONCURRENT_MODE;
                provisionStatus.addExtraParams(AlinkConstants.KEY_CACHE_START_PROVISION_DEVICE_INFO, string);
                this.f3663a.provisionStatusCallback(provisionStatus);
                return;
            }
            return;
        }
        String lowerCase = JSON.parseObject(str).getString("device_mac_address").toLowerCase();
        this.f3663a.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk returned provision fail, state=" + i + ", msg=" + str).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_PROVISION_EXCEPTION).setExtra(this.f3663a.buildDeviceInfoViaMac(lowerCase));
        this.f3663a.provisionResultCallback(null);
        this.f3663a.scheduleNextConfigTask();
    }
}

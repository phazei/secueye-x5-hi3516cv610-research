package com.aliyun.alink.business.devicecenter.provision.core;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.GatewayMeshStrategy;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: compiled from: GatewayMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class Q implements IDeviceInfoNotifyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ GatewayMeshStrategy f3676a;

    public Q(GatewayMeshStrategy gatewayMeshStrategy) {
        this.f3676a = gatewayMeshStrategy;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo == null || this.f3676a.mConfigParams == null) {
            return;
        }
        if (this.f3676a.provisionHasStopped.get()) {
            ALog.d(GatewayMeshStrategy.TAG, "provision has stopped, return.");
            return;
        }
        if (!this.f3676a.waitForResult.get()) {
            ALog.d(GatewayMeshStrategy.TAG, "provision finished return.");
            return;
        }
        if (!StringUtils.isEqualString(deviceInfo.productKey, this.f3676a.mConfigParams.productKey) || !StringUtils.isEqualString(deviceInfo.deviceName, this.f3676a.mConfigParams.deviceName)) {
            ALog.i(GatewayMeshStrategy.TAG, "onDeviceFound Zero otherDeviceInfo=" + deviceInfo);
            return;
        }
        ALog.i(GatewayMeshStrategy.TAG, "onDeviceFound GatewayMesh Provision Success.");
        this.f3676a.updateCache(deviceInfo, DeviceReportTokenType.APP_TOKEN);
        this.f3676a.waitForResult.set(false);
        this.f3676a.stopBackupCheck();
        this.f3676a.provisionResultCallback(deviceInfo);
        this.f3676a.stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
    public void onFailure(DCErrorCode dCErrorCode) {
    }
}

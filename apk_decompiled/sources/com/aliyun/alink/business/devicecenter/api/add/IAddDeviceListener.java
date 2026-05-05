package com.aliyun.alink.business.devicecenter.api.add;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IAddDeviceListener {
    void onPreCheck(boolean z, DCErrorCode dCErrorCode);

    void onProvisionPrepare(int i);

    void onProvisionStatus(ProvisionStatus provisionStatus);

    void onProvisionedResult(boolean z, DeviceInfo deviceInfo, DCErrorCode dCErrorCode);

    void onProvisioning();
}

package com.aliyun.alink.business.devicecenter.api.add;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IConcurrentAddDeviceListener {
    void onPreCheck(DeviceInfo deviceInfo, boolean z, DCErrorCode dCErrorCode);

    void onProvisionPrepare(DeviceInfo deviceInfo, int i);

    void onProvisionStatus(DeviceInfo deviceInfo, ProvisionStatus provisionStatus);

    void onProvisionedResult(boolean z, DeviceInfo deviceInfo, DCErrorCode dCErrorCode);

    void onProvisioning(DeviceInfo deviceInfo);
}

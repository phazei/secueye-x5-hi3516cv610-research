package com.aliyun.alink.business.devicecenter.config;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IDeviceInfoNotifyListener {
    void onDeviceFound(DeviceInfo deviceInfo);

    void onFailure(DCErrorCode dCErrorCode);
}

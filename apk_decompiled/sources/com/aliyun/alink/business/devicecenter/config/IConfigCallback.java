package com.aliyun.alink.business.devicecenter.config;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;

/* JADX INFO: loaded from: classes.dex */
public interface IConfigCallback extends IDCFailCallback {
    void onStatus(ProvisionStatus provisionStatus);

    void onSuccess(DeviceInfo deviceInfo);
}

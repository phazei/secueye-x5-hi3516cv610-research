package com.aliyun.alink.business.devicecenter.api.discovery;

import com.aliyun.alink.business.devicecenter.base.DCErrorCode;

/* JADX INFO: loaded from: classes.dex */
public interface IDiscovery extends IDeviceDiscoveryListener {
    void onFail(DCErrorCode dCErrorCode);
}

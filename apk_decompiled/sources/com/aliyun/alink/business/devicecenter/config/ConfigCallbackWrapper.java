package com.aliyun.alink.business.devicecenter.config;

import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;

/* JADX INFO: loaded from: classes.dex */
public class ConfigCallbackWrapper {
    public IConfigCallback callback = null;
    public boolean isSuccess = false;
    public DCConfigParams params = null;
    public DCErrorCode errorCode = null;
    public DeviceInfo result = null;
    public boolean needTrack = true;
    public ProvisionStatus provisioningStatus = null;

    public ConfigCallbackWrapper callback(IConfigCallback iConfigCallback) {
        this.callback = iConfigCallback;
        return this;
    }

    public ConfigCallbackWrapper error(DCErrorCode dCErrorCode) {
        this.errorCode = dCErrorCode;
        return this;
    }

    public ConfigCallbackWrapper needTrack(boolean z) {
        this.needTrack = z;
        return this;
    }

    public ConfigCallbackWrapper params(DCConfigParams dCConfigParams) {
        this.params = dCConfigParams;
        return this;
    }

    public ConfigCallbackWrapper result(DeviceInfo deviceInfo) {
        this.result = deviceInfo;
        return this;
    }

    public ConfigCallbackWrapper status(ProvisionStatus provisionStatus) {
        this.provisioningStatus = provisionStatus;
        return this;
    }

    public ConfigCallbackWrapper success(boolean z) {
        this.isSuccess = z;
        return this;
    }
}

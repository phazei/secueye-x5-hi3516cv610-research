package com.aliyun.alink.business.devicecenter.api.config;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionConfigParams {
    public List<String> deviceApPrefixList = null;
    public boolean ignoreSoftAPRecoverWiFi = false;
    public boolean ignoreLocationPermissionCheck = false;
    public boolean handleBleSubType4Device = false;
    public long timeWindow = 0;
    public boolean enableGlobalCloudToken = false;

    public static ProvisionConfigParams copy(ProvisionConfigParams provisionConfigParams) {
        if (provisionConfigParams == null) {
            return null;
        }
        ProvisionConfigParams provisionConfigParams2 = new ProvisionConfigParams();
        provisionConfigParams2.deviceApPrefixList = provisionConfigParams.deviceApPrefixList;
        provisionConfigParams2.ignoreSoftAPRecoverWiFi = provisionConfigParams.ignoreSoftAPRecoverWiFi;
        provisionConfigParams2.ignoreLocationPermissionCheck = provisionConfigParams.ignoreLocationPermissionCheck;
        provisionConfigParams2.enableGlobalCloudToken = provisionConfigParams.enableGlobalCloudToken;
        provisionConfigParams2.handleBleSubType4Device = provisionConfigParams.handleBleSubType4Device;
        provisionConfigParams2.timeWindow = provisionConfigParams.timeWindow;
        return provisionConfigParams2;
    }

    public String toString() {
        return "{deviceApPrefixList:" + this.deviceApPrefixList + ", ignoreSoftAPRecoverWiFi:" + this.ignoreSoftAPRecoverWiFi + ", ignoreLocationPermissionCheck:" + this.ignoreLocationPermissionCheck + ", enableGlobalCloudToken:" + this.enableGlobalCloudToken + ", timeWindow:" + this.timeWindow + "}";
    }
}

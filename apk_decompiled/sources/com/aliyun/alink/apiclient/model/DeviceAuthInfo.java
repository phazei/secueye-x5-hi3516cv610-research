package com.aliyun.alink.apiclient.model;

import com.aliyun.alink.apiclient.InitializeConfig;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAuthInfo {
    public String accessKeyId;
    public String accessSecret;
    public String deviceName;
    public String deviceSecret;
    public String productKey;
    public String productSecret;

    public DeviceAuthInfo(InitializeConfig initializeConfig) {
        this.productKey = null;
        this.deviceName = null;
        this.deviceSecret = null;
        this.productSecret = null;
        this.accessKeyId = null;
        this.accessSecret = null;
        if (initializeConfig != null) {
            this.productKey = initializeConfig.productKey;
            this.deviceName = initializeConfig.deviceName;
            this.deviceSecret = initializeConfig.deviceSecret;
            this.productSecret = initializeConfig.productSecret;
            this.accessKeyId = initializeConfig.accessKeyId;
            this.accessSecret = initializeConfig.accessSecret;
        }
    }
}

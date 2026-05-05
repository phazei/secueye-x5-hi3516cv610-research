package com.aliyun.alink.linksdk.tmp.config;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultProvisionConfig extends DefaultClientConfig {
    private AuthChangeType mAuthChangeType;
    private boolean mAutoChangeAuth;

    public DefaultProvisionConfig(DeviceBasicData deviceBasicData) {
        super(deviceBasicData);
        this.mAutoChangeAuth = true;
        this.mAuthChangeType = AuthChangeType.LocalAuth;
        this.mDeviceType = DeviceConfig.DeviceType.PROVISION;
    }

    public boolean IsAutoChangeAuth() {
        return this.mAutoChangeAuth;
    }

    public AuthChangeType getAuthChangeType() {
        return this.mAuthChangeType;
    }

    public void setAuthChangeType(AuthChangeType authChangeType) {
        this.mAuthChangeType = authChangeType;
    }

    public void setAutoChangeAuth(boolean z) {
        this.mAutoChangeAuth = z;
    }

    public enum AuthChangeType {
        LocalAuth(1),
        CloudAuth(2);

        private int value;

        AuthChangeType(int i) {
            this.value = i;
        }
    }
}

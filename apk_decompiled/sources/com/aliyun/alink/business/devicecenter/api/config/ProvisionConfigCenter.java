package com.aliyun.alink.business.devicecenter.api.config;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionConfigCenter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public ProvisionConfigParams f3296a;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final ProvisionConfigCenter f3297a = new ProvisionConfigCenter();
    }

    public static ProvisionConfigCenter getInstance() {
        return SingletonHolder.f3297a;
    }

    public boolean enableGlobalCloudToken() {
        ProvisionConfigParams provisionConfigParams = this.f3296a;
        return provisionConfigParams != null && provisionConfigParams.enableGlobalCloudToken;
    }

    public ProvisionConfigParams getProvisionConfigParams() {
        ProvisionConfigParams provisionConfigParams = this.f3296a;
        if (provisionConfigParams == null) {
            return null;
        }
        return ProvisionConfigParams.copy(provisionConfigParams);
    }

    public boolean handleBleSubType4Device() {
        ProvisionConfigParams provisionConfigParams = this.f3296a;
        return provisionConfigParams != null && provisionConfigParams.handleBleSubType4Device;
    }

    public boolean ignoreLocationPermissionCheck() {
        ProvisionConfigParams provisionConfigParams = this.f3296a;
        return provisionConfigParams != null && provisionConfigParams.ignoreLocationPermissionCheck;
    }

    public boolean ignoreSoftAPRecoverWiFi() {
        ProvisionConfigParams provisionConfigParams = this.f3296a;
        return provisionConfigParams != null && provisionConfigParams.ignoreSoftAPRecoverWiFi;
    }

    public void init(Context context) {
        if (context == null) {
            ALog.e("ProvisionConfigCenter", "init failed, context is null.");
        } else {
            DeviceCenterBiz.getInstance().setAppContext(context.getApplicationContext());
        }
    }

    public void setProvisionConfiguration(ProvisionConfigParams provisionConfigParams) {
        ALog.d("ProvisionConfigCenter", "setProvisionConfiguration() called with: provisionConfigParams = [" + provisionConfigParams + "]");
        if (provisionConfigParams == null) {
            throw new IllegalArgumentException("invalid method params, return.");
        }
        this.f3296a = ProvisionConfigParams.copy(provisionConfigParams);
    }

    public ProvisionConfigCenter() {
        this.f3296a = null;
    }
}

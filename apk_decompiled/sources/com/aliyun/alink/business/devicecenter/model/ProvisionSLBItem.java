package com.aliyun.alink.business.devicecenter.model;

import datasource.bean.ConfigurationData;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public final class ProvisionSLBItem implements Serializable {
    public String authDevice;
    public boolean authFlag;
    public ConfigurationData configurationInfo;
    public String confirmCloud;
    public String deviceName;
    public String discoveredSource;
    public String gatewayDeviceName;
    public String gatewayIotId;
    public String gatewayProductKey;
    public String iotId;
    public String mac;
    public String productId;
    public String productKey;
    public String random;
    public String subDeviceId;

    public String getAuthDevice() {
        return this.authDevice;
    }

    public ConfigurationData getConfigurationInfo() {
        return this.configurationInfo;
    }

    public String getConfirmCloud() {
        return this.confirmCloud;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getDiscoveredSource() {
        return this.discoveredSource;
    }

    public String getGatewayDeviceName() {
        return this.gatewayDeviceName;
    }

    public String getGatewayIotId() {
        return this.gatewayIotId;
    }

    public String getGatewayProductKey() {
        return this.gatewayProductKey;
    }

    public String getIotId() {
        return this.iotId;
    }

    public String getMac() {
        return this.mac;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public String getRandom() {
        return this.random;
    }

    public String getSubDeviceId() {
        return this.subDeviceId;
    }

    public boolean isAuthFlag() {
        return this.authFlag;
    }

    public void setAuthDevice(String str) {
        this.authDevice = str;
    }

    public void setAuthFlag(boolean z) {
        this.authFlag = z;
    }

    public void setConfigurationInfo(ConfigurationData configurationData) {
        this.configurationInfo = configurationData;
    }

    public void setConfirmCloud(String str) {
        this.confirmCloud = str;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setDiscoveredSource(String str) {
        this.discoveredSource = str;
    }

    public void setGatewayDeviceName(String str) {
        this.gatewayDeviceName = str;
    }

    public void setGatewayIotId(String str) {
        this.gatewayIotId = str;
    }

    public void setGatewayProductKey(String str) {
        this.gatewayProductKey = str;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setRandom(String str) {
        this.random = str;
    }

    public void setSubDeviceId(String str) {
        this.subDeviceId = str;
    }
}

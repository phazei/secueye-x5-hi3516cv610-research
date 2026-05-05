package com.alibaba.ailabs.iot.mesh.bean;

/* JADX INFO: loaded from: classes.dex */
public class MeshDeviceInfo {
    public String devId;
    public boolean isLowCostMeshDevice;
    public boolean isLowPower;
    public String productKey;

    public MeshDeviceInfo() {
    }

    public String getDevId() {
        return this.devId;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public boolean isLowCostMeshDevice() {
        return this.isLowCostMeshDevice;
    }

    public boolean isLowPower() {
        return this.isLowPower;
    }

    public void setDevId(String str) {
        this.devId = str;
    }

    public void setLowCostMeshDevice(boolean z) {
        this.isLowCostMeshDevice = z;
    }

    public void setLowPower(boolean z) {
        this.isLowPower = z;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public MeshDeviceInfo(String str, String str2, boolean z, boolean z2) {
        this.devId = str;
        this.productKey = str2;
        this.isLowCostMeshDevice = z;
        this.isLowPower = z2;
    }
}

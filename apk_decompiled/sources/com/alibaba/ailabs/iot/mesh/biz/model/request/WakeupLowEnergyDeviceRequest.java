package com.alibaba.ailabs.iot.mesh.biz.model.request;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes.dex */
public class WakeupLowEnergyDeviceRequest extends BaseApiRequest {
    public String deviceId;
    public String productKey;
    public int wakeUpTime;
    public String REQUEST_METHOD = "POST";
    public String API_HOST = "";
    public String API_PATH = "/living/mesh/device/lowenergy/wakeup";
    public String API_VERSION = "1.0.0";

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public int getWakeUpTime() {
        return this.wakeUpTime;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setWakeUpTime(int i) {
        this.wakeUpTime = i;
    }
}

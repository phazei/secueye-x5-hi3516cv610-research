package com.aliyun.alink.business.devicecenter.channel.coap.response;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class DevicePayload implements Serializable {
    public static final String TAG = "DevicePayload";
    public String cipherType;
    public String deviceName;
    public String fwVersion;
    public String ip;
    public String mac;
    public String productKey;
    public String remainTime;
    public String service;
    public String token;
    public String type;

    public DevicePayload() {
        this.productKey = null;
        this.deviceName = null;
        this.mac = null;
        this.ip = null;
        this.cipherType = null;
        this.token = null;
        this.remainTime = null;
        this.type = null;
        this.service = null;
        this.fwVersion = null;
    }

    public int getRemainTime() {
        if (!TextUtils.isEmpty(this.remainTime) && TextUtils.isDigitsOnly(this.remainTime)) {
            try {
                return Integer.parseInt(this.remainTime);
            } catch (Exception e) {
                ALog.w(TAG, "isRemainTimeValid remainTime=" + this.remainTime + ",e=" + e);
            }
        }
        return -1;
    }

    public String toString() {
        return "{\"ip\":\"" + this.ip + "\",\"mac\":\"" + this.mac + "\",\"productKey\":\"" + this.productKey + "\",\"deviceName\":\"" + this.deviceName + "\",\"cipherType\":\"" + this.cipherType + "\",\"remainTime\":\"" + this.remainTime + "\",\"type\":\"" + this.type + "\",\"token\":\"" + this.token + "\",\"service\":\"" + this.service + "\",\"fwVersion\":\"" + this.fwVersion + "\"}";
    }

    public DevicePayload(DevicePayload devicePayload) {
        this.productKey = null;
        this.deviceName = null;
        this.mac = null;
        this.ip = null;
        this.cipherType = null;
        this.token = null;
        this.remainTime = null;
        this.type = null;
        this.service = null;
        this.fwVersion = null;
        this.productKey = devicePayload.productKey;
        this.deviceName = devicePayload.deviceName;
        this.mac = devicePayload.mac;
        this.ip = devicePayload.ip;
        this.cipherType = devicePayload.cipherType;
        this.token = devicePayload.token;
        this.remainTime = devicePayload.remainTime;
        this.type = devicePayload.type;
        this.service = devicePayload.service;
        this.fwVersion = devicePayload.fwVersion;
    }
}

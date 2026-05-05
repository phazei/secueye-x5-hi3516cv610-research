package com.aliyun.alink.business.devicecenter.discover;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.ICacheModel;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class CloudEnrolleeDeviceModel implements ICacheModel, Serializable {
    public String bssid;
    public String enrolleeDeviceName;
    public String enrolleeIotId;
    public String enrolleeProductKey;
    public String gmtCreate;
    public String gmtModified;
    public String identityId;
    public String regDeviceName;
    public String regIotId;
    public String regProductKey;
    public String rssi;
    public String ssid;
    public String status;
    public String type;

    public boolean equals(Object obj) {
        if (!(obj instanceof CloudEnrolleeDeviceModel)) {
            return super.equals(obj);
        }
        CloudEnrolleeDeviceModel cloudEnrolleeDeviceModel = (CloudEnrolleeDeviceModel) obj;
        return StringUtils.isEqualString(cloudEnrolleeDeviceModel.enrolleeProductKey, this.enrolleeProductKey) && StringUtils.isEqualString(cloudEnrolleeDeviceModel.enrolleeDeviceName, this.enrolleeDeviceName) && StringUtils.isEqualString(cloudEnrolleeDeviceModel.regProductKey, this.regProductKey) && StringUtils.isEqualString(cloudEnrolleeDeviceModel.regDeviceName, this.regDeviceName);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        return this.enrolleeProductKey + "&&" + this.enrolleeDeviceName + "&&" + this.regProductKey + "&&" + this.regDeviceName;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return (TextUtils.isEmpty(this.enrolleeProductKey) || TextUtils.isEmpty(this.enrolleeDeviceName) || TextUtils.isEmpty(this.regProductKey) || TextUtils.isEmpty(this.regDeviceName)) ? false : true;
    }

    public String toString() {
        return "{\"regProductKey\":\"" + this.regProductKey + "\",\"regDeviceName\":\"" + this.regDeviceName + "\",\"enrolleeProductKey\":\"" + this.enrolleeProductKey + "\",\"enrolleeDeviceName\":\"" + this.enrolleeDeviceName + "\",\"bssid\":\"" + this.bssid + "\",\"ssid\":\"" + this.ssid + "\",\"rssi\":\"" + this.rssi + "\",\"type\":\"" + this.type + "\",\"status\":\"" + this.status + "\"}";
    }
}

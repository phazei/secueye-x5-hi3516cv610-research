package com.aliyun.alink.business.devicecenter.cache;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.LKDeviceInfo;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;

/* JADX INFO: loaded from: classes.dex */
public class DeviceInfoICacheModel extends LKDeviceInfo {
    public String token = null;
    public DeviceReportTokenType deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
    public long aliveTime = 0;

    public boolean equals(Object obj) {
        if (!(obj instanceof DeviceInfoICacheModel)) {
            return false;
        }
        DeviceInfoICacheModel deviceInfoICacheModel = (DeviceInfoICacheModel) obj;
        return StringUtils.isEqualString(this.productKey, deviceInfoICacheModel.productKey) && StringUtils.isEqualString(this.deviceName, deviceInfoICacheModel.deviceName);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.LKDeviceInfo, com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return (TextUtils.isEmpty(this.productKey) || TextUtils.isEmpty(this.deviceName) || TextUtils.isEmpty(this.token)) ? false : true;
    }

    public String toString() {
        return "{\"productKey\":\"" + this.productKey + "\",\"deviceName\":\"" + this.deviceName + "\",\"productId\":\"" + this.productId + "\",\"token\":\"" + this.token + "\",\"aliveTime\":\"" + this.aliveTime + "\",\"deviceReportTokenType\":\"" + this.deviceReportTokenType + "\"}";
    }
}

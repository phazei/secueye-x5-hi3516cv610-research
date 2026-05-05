package com.alibaba.ailabs.iot.mesh.ut;

import android.text.TextUtils;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class UtTraceInfo {
    public String deviceId;
    public String productKey;
    public int unicastAddress;
    public String utTraceId;

    public UtTraceInfo(int i, String str, String str2, String str3) {
        this.unicastAddress = i;
        this.utTraceId = str == null ? "" : str;
        this.deviceId = str2 == null ? "" : str2;
        this.productKey = str3 == null ? "" : str3;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || UtTraceInfo.class != obj.getClass()) {
            return false;
        }
        UtTraceInfo utTraceInfo = (UtTraceInfo) obj;
        return this.unicastAddress == utTraceInfo.unicastAddress && Objects.equals(this.utTraceId, utTraceInfo.utTraceId) && Objects.equals(this.deviceId, utTraceInfo.deviceId) && Objects.equals(this.productKey, utTraceInfo.productKey);
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public int getUnicastAddress() {
        return this.unicastAddress;
    }

    public String getUtTraceId() {
        return this.utTraceId;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.unicastAddress), this.utTraceId, this.deviceId, this.productKey);
    }

    public void setDeviceId(String str) {
        if (TextUtils.isEmpty(str)) {
            this.deviceId = "";
        } else {
            this.deviceId = str;
        }
    }

    public void setProductKey(String str) {
        if (TextUtils.isEmpty(str)) {
            this.productKey = "";
        } else {
            this.productKey = str;
        }
    }

    public void setUnicastAddress(int i) {
        this.unicastAddress = i;
    }

    public void setUtTraceId(String str) {
        if (TextUtils.isEmpty(str)) {
            this.utTraceId = "";
        } else {
            this.utTraceId = str;
        }
    }

    public String toString() {
        return "UtTraceInfo{unicastAddress=" + this.unicastAddress + ", utTraceId='" + this.utTraceId + "', deviceId='" + this.deviceId + "', productKey='" + this.productKey + "'}";
    }

    public UtTraceInfo() {
        this.unicastAddress = 0;
        this.utTraceId = "";
        this.productKey = "";
        this.deviceId = "";
    }
}

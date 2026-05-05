package com.aliyun.alink.business.devicecenter.api.add;

import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class RegionInfo implements Serializable {
    public int shortRegionId = -1;
    public String mqttUrl = null;
    public String dyHttpsUrl = null;

    public String toString() {
        return "{\"shortRegionId\":\"" + this.shortRegionId + "\",\"mqttUrl\":\"" + this.mqttUrl + "\",\"dyHttpsUrl\":\"" + this.dyHttpsUrl + "\"}";
    }
}

package com.aliyun.alink.linksdk.alcs.lpbs.data;

/* JADX INFO: loaded from: classes2.dex */
public class PalReqMessage<ExtraReqData> {
    public PalDeviceInfo deviceInfo;
    public ExtraReqData extraReqData;
    public String iotId;
    public Object palOptions;
    public byte[] payload;
    public String topic;

    public String getDevId() {
        PalDeviceInfo palDeviceInfo = this.deviceInfo;
        if (palDeviceInfo != null) {
            return palDeviceInfo.getDevId();
        }
        return null;
    }

    public String toString() {
        return "deviceInfo:" + this.deviceInfo + "topic:" + this.topic;
    }
}

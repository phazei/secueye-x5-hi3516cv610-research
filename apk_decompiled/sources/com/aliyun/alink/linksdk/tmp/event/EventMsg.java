package com.aliyun.alink.linksdk.tmp.event;

import com.aliyun.alink.linksdk.tmp.device.payload.EventNotifyData;

/* JADX INFO: loaded from: classes2.dex */
public class EventMsg {
    protected String deviceId;
    protected EventNotifyData eventNotifyData;
    protected long timeStamp;

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String str) {
        this.deviceId = str;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public EventNotifyData getEventNotifyData() {
        return this.eventNotifyData;
    }

    public void setEventNotifyData(EventNotifyData eventNotifyData) {
        this.eventNotifyData = eventNotifyData;
    }
}

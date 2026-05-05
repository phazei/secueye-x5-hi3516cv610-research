package com.aliyun.alink.linksdk.alcs.data.ica;

/* JADX INFO: loaded from: classes2.dex */
public class ICAReqMessage {
    public static final int RSP_TYPE_NEEDRSP = 0;
    public static final int RSP_TYPE_NORSP = 1;
    public int code;
    public ICADeviceInfo deviceInfo;
    public int groupId;
    public byte[] payload;
    public int rspType;
    public String topic;
    public int type;

    public String getDevId() {
        ICADeviceInfo iCADeviceInfo = this.deviceInfo;
        if (iCADeviceInfo != null) {
            return iCADeviceInfo.getDevId();
        }
        return null;
    }
}

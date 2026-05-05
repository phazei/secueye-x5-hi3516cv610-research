package com.aliyun.alink.linksdk.tmp.device.panel.data.group;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceLocalStatusChangePayload {
    public String method = "group.status";
    public DeviceLocalStatusChangeParams params;

    public DeviceLocalStatusChangePayload(DeviceLocalStatusChangeParams deviceLocalStatusChangeParams) {
        this.params = deviceLocalStatusChangeParams;
    }

    public static class DeviceLocalStatusChangeParams {
        public List<DeviceLocalStatus> deviceLocalStatus;

        public DeviceLocalStatusChangeParams(List<DeviceLocalStatus> list) {
            this.deviceLocalStatus = list;
        }
    }

    public static class DeviceLocalStatus {
        public String deviceName;
        public String iotId;
        public String productKey;
        public Status status;

        public DeviceLocalStatus(String str, String str2, String str3, Status status) {
            this.iotId = str;
            this.productKey = str2;
            this.deviceName = str3;
            this.status = status;
        }
    }

    public static class Status {
        public long time;
        public int value;

        public Status(long j, int i) {
            this.time = j;
            this.value = i;
        }
    }
}

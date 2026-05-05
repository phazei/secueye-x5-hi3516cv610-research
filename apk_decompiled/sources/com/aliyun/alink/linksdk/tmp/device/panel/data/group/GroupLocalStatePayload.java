package com.aliyun.alink.linksdk.tmp.device.panel.data.group;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class GroupLocalStatePayload extends CommonResponsePayload<GroupLocalStateData> {

    public static class DeviceLocalStatus {
        public String deviceName;
        public String iotId;
        public int localOnLineSubStatus;
        public String productKey;
        public int status;
        public long time;
    }

    public static class GroupLocalStateData {
        public List<DeviceLocalStatus> deviceLocalStatus = new ArrayList();
        public int groupLocalStatus;
    }
}

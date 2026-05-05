package com.aliyun.alink.linksdk.tmp.device.payload.property;

import com.aliyun.alink.linksdk.tmp.device.payload.service.DeviceItem;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class BatchSetPropRequest extends GateWayRequest {
    public List<DeviceItem> deviceList;
    public Object items;

    public BatchSetPropRequest() {
        super(GateWayResponse.class);
        this.path = "/living/device/properties/batch/set";
        this.deviceList = new ArrayList();
    }
}

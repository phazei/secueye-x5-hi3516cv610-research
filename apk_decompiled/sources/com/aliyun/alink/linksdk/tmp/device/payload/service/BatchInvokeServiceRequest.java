package com.aliyun.alink.linksdk.tmp.device.payload.service;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class BatchInvokeServiceRequest extends GateWayRequest {
    public Map<String, Object> args;
    public List<DeviceItem> deviceList;
    public String identifier;

    public BatchInvokeServiceRequest() {
        super(GateWayResponse.class);
        this.path = "/living/device/service/batch/invoke";
        this.deviceList = new ArrayList();
    }
}

package com.aliyun.alink.linksdk.tmp.device.payload.service;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class GroupInvokeServiceRequest extends GateWayRequest {
    public Map<String, Object> args;
    public String controlGroupId;
    public String identifier;

    public GroupInvokeServiceRequest() {
        super(GateWayResponse.class);
        this.path = "/living/controlgroup/service/invoke";
    }
}

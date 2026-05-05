package com.aliyun.alink.linksdk.tmp.device.request.propertyalias;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;

/* JADX INFO: loaded from: classes2.dex */
public class UpdatePropertyAliasRequest extends GateWayRequest {
    public String aliasId;
    public String aliasValue;
    public String iotId;

    public UpdatePropertyAliasRequest(String str, String str2, String str3) {
        this.path = "/living/alias/update";
        this.iotId = str;
        this.aliasId = str2;
        this.aliasValue = str3;
    }
}

package com.aliyun.alink.linksdk.tmp.connect.mix;

import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class MTopAndApiGMixResponse extends GateWayResponse {
    public Object data;

    public MTopAndApiGMixResponse() {
        this(null);
    }

    public MTopAndApiGMixResponse(Object obj) {
        this.data = obj;
        this.code = 200;
    }

    public AResponse buildAResponse() {
        AResponse aResponse = new AResponse();
        aResponse.data = JSON.toJSONString(this);
        return aResponse;
    }
}

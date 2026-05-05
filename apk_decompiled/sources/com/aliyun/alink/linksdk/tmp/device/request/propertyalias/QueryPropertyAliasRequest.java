package com.aliyun.alink.linksdk.tmp.device.request.propertyalias;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class QueryPropertyAliasRequest extends GateWayRequest {
    public String iotId;

    public static class QueryPropertyAliasData {
        public String aliasId;
        public String aliasValue;
    }

    public static class QueryPropertyAliasResponse extends GateWayResponse<List<QueryPropertyAliasData>> {
    }

    public QueryPropertyAliasRequest(String str) {
        super(QueryPropertyAliasResponse.class);
        this.path = "/living/alias/batch/get";
        this.iotId = str;
    }
}

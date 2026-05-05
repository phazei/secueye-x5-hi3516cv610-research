package com.aliyun.alink.linksdk.tmp.device.request.localgroup;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;

/* JADX INFO: loaded from: classes2.dex */
public class QueryLocalGroupRequest extends GateWayRequest {
    public int pageNo;
    public int pageSize;

    public static class QueryLocalGroupData {
        public QueryLocalGroupDataList[] list;
        public int total;
    }

    public static class QueryLocalGroupDataList {
        public String accessKey;
        public String accessToken;
        public String localGroupId;
    }

    public static class QueryLocalGroupResponse extends GateWayResponse<QueryLocalGroupData> {
    }

    public QueryLocalGroupRequest() {
        super(QueryLocalGroupResponse.class);
        this.path = "/living/alcs/localgroup/query";
        this.pageNo = 1;
        this.pageSize = 100;
    }
}

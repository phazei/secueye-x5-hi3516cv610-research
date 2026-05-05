package com.aliyun.alink.linksdk.tmp.device.request.localgroup;

import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayResponse;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class QueryLocalGroupDeviceRequest extends GateWayRequest {
    public String localGroupId;
    public int pageNo;
    public int pageSize;

    public static class QueryLocalGroupDeviceResData {
        public List<QueryLocalGroupDeviceResDataInner> items;
        public int totalNum;
    }

    public static class QueryLocalGroupDeviceResponse extends GateWayResponse<QueryLocalGroupDeviceResData> {
    }

    public QueryLocalGroupDeviceRequest() {
        super(QueryLocalGroupDeviceResponse.class);
        this.path = "/living/alcs/localgroup/device/query";
        this.pageNo = 1;
        this.pageSize = 100;
    }

    public static class QueryLocalGroupDeviceResDataInner {
        public String deviceName;
        public String iotId;
        public String productKey;
        public String syncStatus;

        public String getDeviceId() {
            return TextHelper.combineStr(this.productKey, this.deviceName);
        }
    }
}

package com.aliyun.alink.linksdk.cmp.connect.alcs;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;

/* JADX INFO: loaded from: classes2.dex */
public class CoAPRequest<ExtraReqData> extends ARequest {
    public AlcsCoAPConstant.Code code;
    public ExtraReqData extraReqData;
    public String ip;
    public boolean isSecurity;
    public Object payload;
    public int port;
    public int rspType;
    public String topic;
    public AlcsCoAPConstant.Type type;
    public String traceId = "";
    public String alinkIdForTracker = "";
}

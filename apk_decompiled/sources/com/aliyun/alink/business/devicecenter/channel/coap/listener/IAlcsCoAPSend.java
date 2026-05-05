package com.aliyun.alink.business.devicecenter.channel.coap.listener;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;

/* JADX INFO: loaded from: classes.dex */
public interface IAlcsCoAPSend {
    boolean cancelMessage(long j);

    long sendRequest(AlcsCoAPRequest alcsCoAPRequest, IAlcsCoAPReqHandler iAlcsCoAPReqHandler);

    boolean sendResponse(AlcsCoAPResponse alcsCoAPResponse);
}

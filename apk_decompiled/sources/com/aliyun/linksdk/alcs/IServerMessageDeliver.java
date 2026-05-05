package com.aliyun.linksdk.alcs;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;

/* JADX INFO: loaded from: classes2.dex */
public interface IServerMessageDeliver extends IAlcsCoAPResHandler {
    void onRecSecurityRequest(AlcsCoAPContext alcsCoAPContext, AlcsCoAPRequest alcsCoAPRequest);
}

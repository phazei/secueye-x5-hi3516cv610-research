package com.aliyun.linksdk.alcs;

import com.aliyun.alink.linksdk.alcs.api.client.AlcsClientConfig;
import com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IAlcsClient {
    void destroy();

    String getDstAddr();

    void init(AlcsClientConfig alcsClientConfig, IDeviceHandler iDeviceHandler);

    boolean isServerOnline();

    boolean sendRequest(boolean z, PalReqMessage palReqMessage, PalMsgListener palMsgListener);

    boolean sendResponse(boolean z, AlcsCoAPResponse alcsCoAPResponse);

    void setNotifyListener(IClientNotify iClientNotify);

    void subscribe(boolean z, PalSubMessage palSubMessage, PalMsgListener palMsgListener);

    void unsubscribe(boolean z, PalSubMessage palSubMessage, PalMsgListener palMsgListener);
}

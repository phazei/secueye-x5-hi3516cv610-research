package com.aliyun.alink.business.devicecenter.channel.coap.listener;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;

/* JADX INFO: loaded from: classes.dex */
public interface IAlcsCoAP extends IAlcsCoAPSend {
    void deInitCoAPService();

    long getContextId();

    void initCoAPService(AlcsCoAPContext alcsCoAPContext, AlcsCoAPResource alcsCoAPResource);

    void startCoAPService();

    void stopCoAPService();
}

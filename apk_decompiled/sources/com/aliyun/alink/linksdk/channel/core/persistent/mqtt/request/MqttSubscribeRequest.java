package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request;

import com.aliyun.alink.linksdk.channel.core.persistent.PersistentRequest;

/* JADX INFO: loaded from: classes2.dex */
public class MqttSubscribeRequest extends PersistentRequest {
    public boolean isSubscribe;
    public MqttSubscribeRequestParams subscribeRequestParams;
    public String topic;
}

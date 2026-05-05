package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.request;

import com.aliyun.alink.linksdk.channel.core.persistent.PersistentRequest;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes2.dex */
public class MqttRrpcRequest extends PersistentRequest {
    public String replyTopic;
    public String topic;

    public void setTopic(String str) {
        this.topic = str;
        this.replyTopic = str + TmpConstant.URI_TOPIC_REPLY_POST;
    }
}

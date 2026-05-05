package com.aliyun.alink.linksdk.channel.core.persistent.mqtt.send;

import com.aliyun.alink.linksdk.channel.core.base.ISendStatus;

/* JADX INFO: loaded from: classes2.dex */
public enum MqttSendStatus implements ISendStatus {
    waitingToSend,
    waitingToSubReply,
    subReplyed,
    waitingToPublish,
    published,
    waitingToComplete,
    completed
}

package com.alibaba.sdk.android.push.common.a;

import receiver.PushReceiver;

/* JADX INFO: loaded from: classes.dex */
public enum a {
    CHANNEL_SERVICE("com.taobao.accs.ChannelService", "service", true),
    KERNEL_SERVICE("com.taobao.accs.ChannelService$KernelService", "service", true),
    ACCS_JOB_SERVICE("com.taobao.accs.internal.AccsJobService", "service", true),
    MSG_DISTRIBUTE_SERVICE("com.taobao.accs.data.MsgDistributeService", "service", true),
    EVENT_RECEIVER("com.taobao.accs.EventReceiver", PushReceiver.REC_TAG, false),
    SERVICE_RECEIVER("com.taobao.accs.ServiceReceiver", PushReceiver.REC_TAG, true),
    AGOO_SERVICE("org.android.agoo.accs.AgooService", "service", true),
    ALIYUN_PUSH_INTENT_SERVICE("com.aliyun.ams.emas.push.AgooInnerService", "service", true),
    MSG_SERVICE("com.aliyun.ams.emas.push.MsgService", "service", true);

    private final String j;
    private final String k;
    private final boolean l;

    a(String str, String str2, boolean z) {
        this.j = str;
        this.k = str2;
        this.l = z;
    }

    public String a() {
        return this.j;
    }

    public String b() {
        return this.k;
    }

    public boolean c() {
        return this.l;
    }
}

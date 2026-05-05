package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.device.payload.event.EventRequestPayload;

/* JADX INFO: compiled from: TmpCancelSubEventRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends j<a, EventRequestPayload> {
    public a() {
        this.o = false;
        this.j = CommonRequestBuilder.RequestType.RELEATE;
    }

    public static a d() {
        return new a();
    }
}

package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceRequestPayload;

/* JADX INFO: compiled from: TmpServiceRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class l extends j<l, ServiceRequestPayload> {
    protected Option m;

    public static l d() {
        return new l();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public l a(Option option) {
        this.m = option;
        return (l) this.k;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h hVar = (com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h) super.c();
        hVar.a(this.m);
        return hVar;
    }
}

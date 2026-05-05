package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.device.payload.discovery.DevNotifyRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: compiled from: TmpNotifyRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class g extends j<g, DevNotifyRequestPayload> {
    /* JADX WARN: Type inference failed for: r0v4, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.discovery.DevNotifyRequestPayload] */
    protected g() {
        this.h = false;
        this.e = "224.0.1.187";
        this.f4234d = TmpConstant.PATH_NOTIFY;
        b(true);
        this.l = new DevNotifyRequestPayload();
    }

    public static g d() {
        return new g();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public g e(String str) {
        ((DevNotifyRequestPayload) this.l).setDeviceModel(str);
        return this;
    }
}

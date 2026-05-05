package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.device.payload.setup.SetupRequestPayload;

/* JADX INFO: compiled from: TmpSetupRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class n extends j<n, SetupRequestPayload> {
    /* JADX WARN: Type inference failed for: r0v3, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.setup.SetupRequestPayload] */
    public n(String str, String str2) {
        k(str);
        l(str2);
        a(CommonRequestBuilder.Method.PUT);
        m("core.service.setup");
        j("dev");
        this.l = new SetupRequestPayload(str, str2);
    }

    public static n a(String str, String str2) {
        return new n(str, str2);
    }
}

package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.device.payload.permission.DeleteAuthUserRequestPayload;
import java.util.List;

/* JADX INFO: compiled from: TmpDeleteAuthUserRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends j<b, DeleteAuthUserRequestPayload> {
    /* JADX WARN: Type inference failed for: r0v1, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.permission.DeleteAuthUserRequestPayload] */
    public b(String str, String str2) {
        k(str);
        l(str2);
        m("core.service.user");
        this.l = new DeleteAuthUserRequestPayload(str, str2);
    }

    public static b a(String str, String str2) {
        return new b(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public b a(List<String> list) {
        ((DeleteAuthUserRequestPayload) this.l).setIds(list);
        return this;
    }
}

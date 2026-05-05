package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.device.payload.permission.GroupAuthRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: compiled from: TmpGroupAuthRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class f extends j<f, GroupAuthRequestPayload> {
    public static f a(String str, String str2) {
        return new f(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.permission.GroupAuthRequestPayload] */
    public f(String str, String str2) {
        k(str);
        l(str2);
        j("dev");
        m(TmpConstant.METHOD_SERVICE_AUTH_INFO);
        this.l = new GroupAuthRequestPayload(str, str2);
        ((GroupAuthRequestPayload) this.l).setMethod(TmpConstant.METHOD_SERVICE_AUTH_INFO);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f e(String str) {
        ((GroupAuthRequestPayload) this.l).setOp(str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f f(String str) {
        ((GroupAuthRequestPayload) this.l).setDataType(str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f g(String str) {
        ((GroupAuthRequestPayload) this.l).setGroupId(str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f h(String str) {
        ((GroupAuthRequestPayload) this.l).setParam1(str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public f i(String str) {
        ((GroupAuthRequestPayload) this.l).setParam2(str);
        return this;
    }
}

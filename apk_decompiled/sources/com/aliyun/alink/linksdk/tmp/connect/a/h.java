package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.tmp.device.payload.permission.PutAuthUserRequestPayload;

/* JADX INFO: compiled from: TmpPutAuthUserReqeustBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class h extends j<h, PutAuthUserRequestPayload> {
    /* JADX WARN: Type inference failed for: r0v1, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.permission.PutAuthUserRequestPayload] */
    public h(String str, String str2) {
        k(str);
        l(str2);
        m("core.service.user");
        this.l = new PutAuthUserRequestPayload(str, str2);
    }

    public static h a(String str, String str2) {
        return new h(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public h e(String str) {
        ((PutAuthUserRequestPayload) this.l).setAuthId(str);
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public h f(String str) {
        ((PutAuthUserRequestPayload) this.l).setToken(str);
        return this;
    }
}

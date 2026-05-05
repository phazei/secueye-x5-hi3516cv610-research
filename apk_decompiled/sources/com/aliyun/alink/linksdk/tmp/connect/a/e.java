package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayRequest;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslRequestPayload;

/* JADX INFO: compiled from: TmpGetTslRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class e extends j<e, GetTslRequestPayload> {
    protected String m = "/thing/tsl/get";
    protected String n = "1.0.0";

    /* JADX WARN: Type inference failed for: r0v2, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.discovery.GetTslRequestPayload] */
    public e() {
        this.l = new GetTslRequestPayload(null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        return new com.aliyun.alink.linksdk.tmp.connect.d(ApiGatewayRequest.build(this.m, this.n, ((GetTslRequestPayload) this.l).getParams()));
    }

    public static e d() {
        return new e();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public e e(String str) {
        ((GetTslRequestPayload) this.l).putIotId(str);
        return this;
    }
}

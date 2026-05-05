package com.aliyun.alink.linksdk.tmp.connect.a;

import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.device.payload.rawdata.SendRawdataRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: compiled from: TmpSendRawDataRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class k extends j<k, SendRawdataRequestPayload> {
    /* JADX WARN: Type inference failed for: r1v1, types: [Payload, com.aliyun.alink.linksdk.tmp.device.payload.rawdata.SendRawdataRequestPayload] */
    protected k(String str, String str2) {
        k(str);
        l(str2);
        this.l = new SendRawdataRequestPayload();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public k a(byte[] bArr) {
        ((SendRawdataRequestPayload) this.l).setData(bArr);
        return this;
    }

    public static k a(String str, String str2) {
        return new k(str, str2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.ip = this.e;
        commonRequest.port = this.f;
        commonRequest.topic = "/" + this.s + "/" + this.p + "/" + this.q + TmpConstant.URI_THING + TmpConstant.URI_MODEL + "/up_raw";
        commonRequest.mothod = b().toCRMethod();
        commonRequest.payload = ((SendRawdataRequestPayload) this.l).getData();
        commonRequest.context = this.f4233c;
        commonRequest.type = Integer.valueOf(this.u);
        commonRequest.isSecurity = this.h;
        com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h hVar = new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h(commonRequest);
        hVar.a(f());
        hVar.b(g());
        hVar.a(this.f4233c);
        hVar.a(this.h);
        return hVar;
    }
}

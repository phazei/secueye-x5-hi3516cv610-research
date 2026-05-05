package com.aliyun.alink.linksdk.tmp.connect.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.device.payload.property.SetPropertyRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;

/* JADX INFO: compiled from: TmpSetPropertyRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class m extends j<m, SetPropertyRequestPayload> {
    protected String m;
    protected Option n;

    public static m d() {
        return new m();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public m e(String str) {
        this.m = str;
        return (m) this.k;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public m a(Option option) {
        this.n = option;
        return (m) this.k;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.ip = this.e;
        commonRequest.port = this.f;
        commonRequest.topic = a(f(), g(), h(), this.s);
        commonRequest.mothod = b().toCRMethod();
        commonRequest.payload = TextUtils.isEmpty(this.g) ? GsonUtils.toJson(this.l) : this.g;
        commonRequest.context = this.f4233c;
        commonRequest.type = Integer.valueOf(this.u);
        commonRequest.isSecurity = this.h;
        commonRequest.traceId = String.valueOf(this.m);
        commonRequest.alinkIdForTracker = String.valueOf(this.l == 0 ? "" : ((SetPropertyRequestPayload) this.l).getId());
        commonRequest.iotId = this.v;
        commonRequest.tgMeshType = this.w;
        com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h hVar = new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h(commonRequest);
        hVar.a(f());
        hVar.b(g());
        hVar.a(this.f4233c);
        hVar.a(this.h);
        hVar.a(this.n);
        return hVar;
    }
}

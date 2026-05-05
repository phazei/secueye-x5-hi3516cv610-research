package com.aliyun.alink.linksdk.tmp.connect.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.DiscoveryRequestPayload;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: compiled from: TmpDiscoverRequestBuilder.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends j<c, DiscoveryRequestPayload> {
    protected c() {
        this.h = false;
        this.e = "224.0.1.187";
        this.f4234d = TmpConstant.PATH_DISCOVERY;
        this.j = CommonRequestBuilder.RequestType.MULTIPLE_RESPONSE;
        c(true);
    }

    public static c d() {
        return new c();
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.a.j, com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder
    public com.aliyun.alink.linksdk.tmp.connect.d c() {
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.ip = this.e;
        commonRequest.port = this.f;
        commonRequest.topic = this.f4234d;
        commonRequest.mothod = b().toCRMethod();
        commonRequest.payload = TextUtils.isEmpty(this.g) ? GsonUtils.toJson(this.l) : this.g;
        commonRequest.context = this.f4233c;
        commonRequest.type = Integer.valueOf(this.u);
        commonRequest.isSecurity = false;
        return new com.aliyun.alink.linksdk.tmp.connect.entity.cmp.h(commonRequest);
    }
}

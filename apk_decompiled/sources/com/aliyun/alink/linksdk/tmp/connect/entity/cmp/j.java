package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CpSubEventHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class j implements IConnectSubscribeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.c f4263a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.d f4264b;

    public j(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        this.f4263a = cVar;
        this.f4264b = dVar;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onSuccess() {
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        AResponse aResponse = new AResponse();
        aResponse.data = GsonUtils.toJson(commonResponsePayload);
        this.f4263a.a(this.f4264b, new i(aResponse));
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onFailure(AError aError) {
        this.f4263a.a(this.f4264b, new ErrorInfo(aError.getCode(), aError.getMsg()));
    }
}

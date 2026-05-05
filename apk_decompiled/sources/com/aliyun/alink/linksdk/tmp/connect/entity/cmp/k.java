package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CpUnsubEventHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class k implements IConnectUnscribeListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.c f4265a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.d f4266b;

    public k(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        this.f4265a = cVar;
        this.f4266b = dVar;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onSuccess() {
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setCode(200);
        AResponse aResponse = new AResponse();
        aResponse.data = GsonUtils.toJson(commonResponsePayload);
        this.f4265a.a(this.f4266b, new i(aResponse));
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onFailure(AError aError) {
        this.f4265a.a(this.f4266b, new ErrorInfo(aError.getCode(), aError.getMsg()));
    }
}

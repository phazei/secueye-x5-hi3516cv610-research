package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CpConnectSendHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class d extends com.aliyun.alink.linksdk.tmp.connect.f implements IConnectSendListener {
    public d(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.c cVar) {
        super(cVar, dVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
    public void onResponse(ARequest aRequest, AResponse aResponse) {
        a(this.f4276d, new i(aResponse));
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
    public void onFailure(ARequest aRequest, AError aError) {
        a(this.f4276d, new ErrorInfo(aError.getCode(), aError.getMsg()));
    }
}

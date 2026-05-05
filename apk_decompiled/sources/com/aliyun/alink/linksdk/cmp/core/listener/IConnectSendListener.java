package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectSendListener {
    void onFailure(ARequest aRequest, AError aError);

    void onResponse(ARequest aRequest, AResponse aResponse);
}

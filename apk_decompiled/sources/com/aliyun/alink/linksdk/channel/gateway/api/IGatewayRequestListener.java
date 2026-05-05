package com.aliyun.alink.linksdk.channel.gateway.api;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IGatewayRequestListener {
    void onFailure(AError aError);

    void onSuccess(String str);
}

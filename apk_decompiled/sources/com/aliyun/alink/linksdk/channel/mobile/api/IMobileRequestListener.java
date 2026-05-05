package com.aliyun.alink.linksdk.channel.mobile.api;

import com.aliyun.alink.linksdk.channel.core.base.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IMobileRequestListener {
    void onFailure(AError aError);

    void onSuccess(String str);
}

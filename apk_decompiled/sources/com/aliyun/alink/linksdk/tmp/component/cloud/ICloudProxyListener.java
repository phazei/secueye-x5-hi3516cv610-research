package com.aliyun.alink.linksdk.tmp.component.cloud;

import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface ICloudProxyListener {
    void onFailure(String str, AError aError);

    void onResponse(String str, Object obj);
}

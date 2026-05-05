package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectPublishResourceListener {
    void onFailure(AResource aResource, AError aError);

    void onSuccess(AResource aResource);
}

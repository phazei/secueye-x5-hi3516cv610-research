package com.aliyun.alink.linksdk.cmp.core.listener;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;

/* JADX INFO: loaded from: classes2.dex */
public interface IResourceRequestListener extends IBaseListener {
    void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener);
}

package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: compiled from: TRawResRespnseCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements ITResResponseCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected IResourceResponseListener f4416a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected ResourceRequest f4417b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected AResource f4418c;

    public d(ResourceRequest resourceRequest, AResource aResource, IResourceResponseListener iResourceResponseListener) {
        this.f4417b = resourceRequest;
        this.f4416a = iResourceResponseListener;
        this.f4418c = aResource;
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback
    public void onComplete(String str, ErrorInfo errorInfo, Object obj) {
        if (errorInfo != null) {
            errorInfo.isSuccess();
        }
        new AResponse().data = obj;
        this.f4416a.onResponse(this.f4418c, this.f4417b, null);
    }
}

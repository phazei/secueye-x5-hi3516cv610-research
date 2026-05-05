package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TResResponseCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class i implements ITResResponseCallback {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f4431d = "[Tmp]TResResponseCallback";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected IResourceResponseListener f4432a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected ResourceRequest f4433b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected AResource f4434c;
    private String e;

    public i(ResourceRequest resourceRequest, AResource aResource, String str, IResourceResponseListener iResourceResponseListener) {
        this.f4433b = resourceRequest;
        this.f4432a = iResourceResponseListener;
        this.f4434c = aResource;
        this.e = str;
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback
    public void onComplete(String str, ErrorInfo errorInfo, Object obj) {
        boolean z = errorInfo == null || errorInfo.isSuccess();
        ALog.d(f4431d, "onComplete identifer:" + str + " mRequestId:" + this.e + " errorInfo:" + errorInfo + " result:" + obj);
        CommonResponsePayload commonResponsePayload = new CommonResponsePayload();
        commonResponsePayload.setId(this.e);
        if (z) {
            commonResponsePayload.setCode(200);
            commonResponsePayload.setData(obj);
        } else {
            commonResponsePayload.setCode(300);
            commonResponsePayload.setData(errorInfo.getErrorMsg());
        }
        AResponse aResponse = new AResponse();
        aResponse.data = GsonUtils.toJson(commonResponsePayload);
        this.f4432a.onResponse(this.f4434c, this.f4433b, aResponse);
    }
}

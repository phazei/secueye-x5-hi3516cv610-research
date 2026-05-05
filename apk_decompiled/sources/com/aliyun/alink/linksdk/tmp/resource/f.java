package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TResPublicWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class f implements IConnectPublishResourceListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4422a = "[Tmp]TResPublicWrapper";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IPublishResourceListener f4423b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4424c;

    public f(String str, IPublishResourceListener iPublishResourceListener) {
        this.f4423b = iPublishResourceListener;
        this.f4424c = str;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectPublishResourceListener
    public void onSuccess(AResource aResource) {
        ALog.d(f4422a, "onSuccess mResId:" + this.f4424c + " mListener:" + this.f4423b + " aResource:" + aResource);
        IPublishResourceListener iPublishResourceListener = this.f4423b;
        if (iPublishResourceListener != null) {
            iPublishResourceListener.onSuccess(this.f4424c, null);
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectPublishResourceListener
    public void onFailure(AResource aResource, AError aError) {
        ALog.d(f4422a, "onSuccess mResId:" + this.f4424c + " mListener:" + this.f4423b + " aResource:" + aResource);
        if (this.f4423b == null || aError == null || aError.getCode() == 517) {
            return;
        }
        this.f4423b.onError(this.f4424c, aError);
    }
}

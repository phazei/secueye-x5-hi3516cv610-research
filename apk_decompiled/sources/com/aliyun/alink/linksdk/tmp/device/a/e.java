package com.aliyun.alink.linksdk.tmp.device.a;

import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;

/* JADX INFO: compiled from: GroupAsyncTask.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class e<Task> extends a<com.aliyun.alink.linksdk.tmp.connect.d, com.aliyun.alink.linksdk.tmp.connect.e> {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected WeakReference<com.aliyun.alink.linksdk.tmp.device.b> f4360c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected IDevListener f4361d;
    protected Task e = this;

    /* JADX WARN: Multi-variable type inference failed */
    public e(com.aliyun.alink.linksdk.tmp.device.b bVar, IDevListener iDevListener) {
        this.f4360c = new WeakReference<>(bVar);
        this.f4361d = iDevListener;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar, ErrorInfo errorInfo) {
        b(dVar, eVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        c(dVar, errorInfo);
    }

    protected void b(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar, ErrorInfo errorInfo) {
        IDevListener iDevListener = this.f4361d;
        if (iDevListener == null) {
            ALog.e("[Tmp]AsyncTask", "onFlowComplete handler empty error");
        } else {
            this.f4361d = null;
            iDevListener.onSuccess(null, null);
        }
    }

    protected void c(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        IDevListener iDevListener = this.f4361d;
        if (iDevListener == null) {
            ALog.w("[Tmp]AsyncTask", "onFlowError empty error");
        } else {
            this.f4361d = null;
            iDevListener.onFail(null, errorInfo);
        }
    }
}

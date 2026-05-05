package com.aliyun.alink.linksdk.tmp.device.a;

import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: compiled from: AsyncTask.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class a<Request, Response> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4309a = "[Tmp]AsyncTask";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected c<Request, Response> f4310b;

    public abstract void a(Request request, ErrorInfo errorInfo);

    public abstract void a(Request request, Response response, ErrorInfo errorInfo);

    public abstract boolean a();

    public boolean a(a aVar, Request request, Response response) {
        return true;
    }

    public void a(c cVar) {
        this.f4310b = cVar;
    }

    public void b(Request request, ErrorInfo errorInfo) {
        c<Request, Response> cVar = this.f4310b;
        if (cVar != null) {
            cVar.a((a) this, (Object) request, errorInfo);
        }
    }

    public void a(Request request, Response response) {
        c<Request, Response> cVar = this.f4310b;
        if (cVar != null) {
            cVar.a(this, request, response);
        }
    }
}

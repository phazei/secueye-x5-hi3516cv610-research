package com.aliyun.alink.linksdk.tmp.device.a;

import android.os.AsyncTask;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/* JADX INFO: compiled from: AsyncTaskFlow.java */
/* JADX INFO: loaded from: classes2.dex */
public class c<Request, Response> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4330a = "[Tmp]AsyncTaskFlow";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected static Executor f4331d;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected List<a> f4332b = new LinkedList();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected a<Request, Response> f4333c;

    public c() {
        if (f4331d == null) {
            f4331d = AsyncTask.THREAD_POOL_EXECUTOR;
        }
    }

    public void a(a aVar, Request request, Response response) {
        b(aVar, request, response);
    }

    public void a(a aVar, Request request, ErrorInfo errorInfo) {
        a<Request, Response> aVar2 = this.f4333c;
        if (aVar2 == null || aVar2 != aVar) {
            return;
        }
        aVar2.a((Object) request, errorInfo);
    }

    public boolean a() {
        if (this.f4332b.isEmpty()) {
            return false;
        }
        this.f4333c = this.f4332b.get(0);
        this.f4333c.a((a) null, (Object) null, (Object) null);
        try {
            f4331d.execute(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.c.1
                @Override // java.lang.Runnable
                public void run() {
                    c.this.f4333c.a();
                }
            });
            return true;
        } catch (Exception e) {
            ALog.e(f4330a, "action error:" + e.toString());
            return true;
        }
    }

    public void a(Request request, Response response, ErrorInfo errorInfo) {
        a<Request, Response> aVar = this.f4333c;
        if (aVar != null) {
            aVar.a(request, response, errorInfo);
        }
    }

    protected void b(a aVar, Request request, Response response) {
        a<Request, Response> aVarA = a(aVar);
        if (aVarA == null) {
            a(request, response, new ErrorInfo(300, "task flow error"));
            return;
        }
        a<Request, Response> aVar2 = this.f4333c;
        if (aVarA == aVar2) {
            return;
        }
        try {
            this.f4333c = aVarA;
            this.f4333c.a(aVar2, request, response);
            f4331d.execute(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.c.2
                @Override // java.lang.Runnable
                public void run() {
                    c.this.f4333c.a();
                }
            });
        } catch (Exception e) {
            this.f4333c.b(request, new ErrorInfo(4001, e.toString()));
        }
    }

    protected a a(a aVar) {
        a<Request, Response> aVar2 = this.f4333c;
        if (aVar != aVar2) {
            return aVar2;
        }
        int iIndexOf = this.f4332b.indexOf(aVar2) + 1;
        if (iIndexOf >= this.f4332b.size()) {
            return null;
        }
        return this.f4332b.get(iIndexOf);
    }

    public c b(a aVar) {
        aVar.a(this);
        this.f4332b.add(aVar);
        return this;
    }
}

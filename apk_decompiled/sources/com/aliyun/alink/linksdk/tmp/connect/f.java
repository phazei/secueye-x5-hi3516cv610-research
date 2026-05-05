package com.aliyun.alink.linksdk.tmp.connect;

import android.os.Looper;
import android.os.Message;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TmpSyncRequestHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class f implements com.aliyun.alink.linksdk.tmp.connect.c, INotifyHandler {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4273a = "[Tmp]TmpSyncRequestHandler";
    public static com.aliyun.alink.linksdk.tmp.device.a.f e;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.c f4274b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected INotifyHandler f4275c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected com.aliyun.alink.linksdk.tmp.connect.d f4276d;
    protected b f;

    /* JADX INFO: compiled from: TmpSyncRequestHandler.java */
    public static abstract class b implements com.aliyun.alink.linksdk.tmp.connect.c {
    }

    public static synchronized void a() {
        if (e == null) {
            e = new com.aliyun.alink.linksdk.tmp.device.a.f(Looper.getMainLooper());
        }
    }

    public f(com.aliyun.alink.linksdk.tmp.connect.c cVar, b bVar, com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        this.f4274b = cVar;
        this.f4276d = dVar;
        this.f = bVar;
        if (b()) {
            LogCat.d(f4273a, "TmpSyncRequestHandler multhead callback");
        } else {
            a();
        }
    }

    public f(com.aliyun.alink.linksdk.tmp.connect.c cVar, com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        this(cVar, null, dVar);
    }

    public f(INotifyHandler iNotifyHandler, com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        this.f4275c = iNotifyHandler;
        this.f4276d = dVar;
        if (b()) {
            LogCat.d(f4273a, "TmpSyncRequestHandler multhead callback");
        } else {
            a();
        }
    }

    public f(INotifyHandler iNotifyHandler) {
        this(iNotifyHandler, (com.aliyun.alink.linksdk.tmp.connect.d) null);
    }

    public f a(com.aliyun.alink.linksdk.tmp.connect.d dVar) {
        this.f4276d = dVar;
        if (b()) {
            LogCat.d(f4273a, "TmpSyncRequestHandler multhead callback");
        } else {
            a();
        }
        return this;
    }

    protected boolean b() {
        com.aliyun.alink.linksdk.tmp.connect.d dVar = this.f4276d;
        return dVar != null && dVar.a();
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        ALog.d(f4273a, "onLoad response :" + eVar);
        if (this.f4274b == null) {
            ALog.e(f4273a, "onLoad handler empty");
            return;
        }
        if (b()) {
            ALog.d(f4273a, "onLoad mulcallback");
            this.f4274b.a(this.f4276d, eVar);
            return;
        }
        ALog.d(f4273a, "onLoad mainthreadcallback");
        Message messageObtain = Message.obtain();
        messageObtain.what = 1;
        messageObtain.obj = new c(this.f4274b, this.f, this.f4276d, eVar);
        e.sendMessage(messageObtain);
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        if (this.f4274b == null) {
            LogCat.e(f4273a, "onError handler empty");
            return;
        }
        if (b()) {
            LogCat.d(f4273a, "onError mulcallback");
            this.f4274b.a(this.f4276d, errorInfo);
            return;
        }
        LogCat.d(f4273a, "onError mainthreadcallback");
        Message messageObtain = Message.obtain();
        messageObtain.what = 2;
        messageObtain.obj = new a(this.f4274b, this.f, this.f4276d, errorInfo);
        e.sendMessage(messageObtain);
    }

    @Override // com.aliyun.alink.linksdk.tmp.event.INotifyHandler
    public void onMessage(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        ALog.d(f4273a, "onMessage ");
        if (this.f4275c == null) {
            LogCat.e(f4273a, "onMessage handler empty");
            return;
        }
        if (b()) {
            LogCat.d(f4273a, "onMessage mulcallback");
            this.f4275c.onMessage(this.f4276d, eVar);
            return;
        }
        LogCat.d(f4273a, "onMessage mainthreadcallback");
        Message messageObtain = Message.obtain();
        messageObtain.what = 3;
        messageObtain.obj = new d(this.f4275c, this.f, this.f4276d, eVar);
        e.sendMessage(messageObtain);
    }

    /* JADX INFO: compiled from: TmpSyncRequestHandler.java */
    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public com.aliyun.alink.linksdk.tmp.connect.c f4281a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public com.aliyun.alink.linksdk.tmp.connect.d f4282b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public e f4283c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public b f4284d;

        public c(com.aliyun.alink.linksdk.tmp.connect.c cVar, b bVar, com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
            this.f4281a = cVar;
            this.f4282b = dVar;
            this.f4283c = eVar;
            this.f4284d = bVar;
        }
    }

    /* JADX INFO: compiled from: TmpSyncRequestHandler.java */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public com.aliyun.alink.linksdk.tmp.connect.c f4277a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public com.aliyun.alink.linksdk.tmp.connect.d f4278b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public ErrorInfo f4279c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public b f4280d;

        public a(com.aliyun.alink.linksdk.tmp.connect.c cVar, b bVar, com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
            this.f4277a = cVar;
            this.f4278b = dVar;
            this.f4279c = errorInfo;
            this.f4280d = bVar;
        }
    }

    /* JADX INFO: compiled from: TmpSyncRequestHandler.java */
    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public INotifyHandler f4285a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public com.aliyun.alink.linksdk.tmp.connect.d f4286b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public e f4287c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public b f4288d;

        public d(INotifyHandler iNotifyHandler, b bVar, com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
            this.f4285a = iNotifyHandler;
            this.f4286b = dVar;
            this.f4287c = eVar;
            this.f4288d = bVar;
        }
    }
}

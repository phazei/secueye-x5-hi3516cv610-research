package anetwork.channel.unified;

import anetwork.channel.interceptor.Callback;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class j {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final anetwork.channel.entity.g f2078a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Callback f2079b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final String f2080c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public volatile AtomicBoolean f2081d = new AtomicBoolean();
    public volatile IUnifiedTask e = null;
    public volatile Future f = null;

    public j(anetwork.channel.entity.g gVar, Callback callback) {
        this.f2078a = gVar;
        this.f2080c = gVar.e;
        this.f2079b = callback;
    }

    public void a() {
        Future future = this.f;
        if (future != null) {
            future.cancel(true);
            this.f = null;
        }
    }

    public void b() {
        if (this.e != null) {
            this.e.cancel();
            this.e = null;
        }
    }
}

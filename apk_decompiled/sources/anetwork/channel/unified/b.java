package anetwork.channel.unified;

import android.text.TextUtils;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.util.StringUtils;
import anetwork.channel.cookie.CookieManager;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class b implements IUnifiedTask {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private j f2054c;
    private Request f;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile boolean f2053b = false;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    volatile Cancelable f2052a = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f2055d = 0;
    private int e = 0;

    static /* synthetic */ int b(b bVar) {
        int i = bVar.e;
        bVar.e = i + 1;
        return i;
    }

    public b(j jVar) {
        this.f2054c = jVar;
        this.f = jVar.f2078a.a();
    }

    @Override // anet.channel.request.Cancelable
    public void cancel() {
        this.f2053b = true;
        if (this.f2052a != null) {
            this.f2052a.cancel();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2053b) {
            return;
        }
        if (this.f2054c.f2078a.i()) {
            String cookie = CookieManager.getCookie(this.f2054c.f2078a.g());
            if (!TextUtils.isEmpty(cookie)) {
                Request.Builder builderNewBuilder = this.f.newBuilder();
                String str = this.f.getHeaders().get("Cookie");
                if (!TextUtils.isEmpty(str)) {
                    cookie = StringUtils.concatString(str, "; ", cookie);
                }
                builderNewBuilder.addHeader("Cookie", cookie);
                this.f = builderNewBuilder.build();
            }
        }
        this.f.f1794a.degraded = 2;
        this.f.f1794a.sendBeforeTime = System.currentTimeMillis() - this.f.f1794a.reqStart;
        anet.channel.session.b.a(this.f, new c(this));
    }
}

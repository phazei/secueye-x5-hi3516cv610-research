package anetwork.channel.cookie;

import anet.channel.util.ALog;
import java.net.HttpCookie;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f2011a;

    b(String str) {
        this.f2011a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (CookieManager.f2006d == null) {
            return;
        }
        try {
            for (HttpCookie httpCookie : HttpCookie.parse(this.f2011a)) {
                if (httpCookie.getName().equals(CookieManager.f2006d.f2007a)) {
                    CookieManager.f2006d.f2008b = httpCookie.toString();
                    CookieManager.f2006d.f2010d = httpCookie.getDomain();
                    CookieManager.f2006d.f2009c = this.f2011a;
                    CookieManager.f2006d.a();
                    return;
                }
            }
        } catch (Exception e) {
            ALog.e(CookieManager.TAG, "cookieMonitorSave error.", null, e, new Object[0]);
        }
    }
}

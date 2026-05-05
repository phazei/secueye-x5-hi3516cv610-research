package anetwork.channel.cookie;

import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.CookieMonitorStat;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import com.huawei.hms.framework.common.ContainerUtils;
import java.net.HttpCookie;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f2012a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f2013b;

    c(String str, String str2) {
        this.f2012a = str;
        this.f2013b = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (CookieManager.f2006d == null) {
            return;
        }
        try {
            if (TextUtils.isEmpty(CookieManager.f2006d.f2007a) || !HttpCookie.domainMatches(CookieManager.f2006d.f2010d, HttpUrl.parse(this.f2012a).host()) || TextUtils.isEmpty(this.f2013b)) {
                return;
            }
            if (this.f2013b.contains(CookieManager.f2006d.f2007a + ContainerUtils.KEY_VALUE_DELIMITER)) {
                return;
            }
            CookieMonitorStat cookieMonitorStat = new CookieMonitorStat(this.f2012a);
            cookieMonitorStat.cookieName = CookieManager.f2006d.f2007a;
            cookieMonitorStat.cookieText = CookieManager.f2006d.f2008b;
            cookieMonitorStat.setCookie = CookieManager.f2006d.f2009c;
            cookieMonitorStat.missType = 1;
            AppMonitor.getInstance().commitStat(cookieMonitorStat);
        } catch (Exception e) {
            ALog.e(CookieManager.TAG, "cookieMonitorReport error.", null, e, new Object[0]);
        }
    }
}

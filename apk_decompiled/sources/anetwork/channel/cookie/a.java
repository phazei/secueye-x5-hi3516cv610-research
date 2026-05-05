package anetwork.channel.cookie;

import android.text.TextUtils;
import anet.channel.util.ALog;
import anetwork.channel.cookie.CookieManager;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class a implements Runnable {
    a() {
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (TextUtils.isEmpty(CookieManager.f())) {
                return;
            }
            CookieManager.a unused = CookieManager.f2006d = new CookieManager.a(CookieManager.f());
        } catch (Exception e) {
            ALog.e(CookieManager.TAG, "", null, e, new Object[0]);
        }
    }
}

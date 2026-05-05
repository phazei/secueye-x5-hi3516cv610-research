package anetwork.channel.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.http.NetworkSdkSetting;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class CookieManager {
    public static final String TAG = "anet.CookieManager";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile boolean f2003a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static android.webkit.CookieManager f2004b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static boolean f2005c = true;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static a f2006d;
    private static SharedPreferences e;

    public static synchronized void setup(Context context) {
        if (NetworkConfigCenter.isCookieEnable()) {
            if (f2003a) {
                return;
            }
            try {
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (Build.VERSION.SDK_INT < 21) {
                    CookieSyncManager.createInstance(context);
                }
                f2004b = android.webkit.CookieManager.getInstance();
                f2004b.setAcceptCookie(true);
                if (Build.VERSION.SDK_INT < 21) {
                    f2004b.removeExpiredCookie();
                }
                e = PreferenceManager.getDefaultSharedPreferences(context);
                e();
                ALog.e(TAG, "CookieManager setup.", null, "cost", Long.valueOf(System.currentTimeMillis() - jCurrentTimeMillis));
            } catch (Throwable th) {
                f2005c = false;
                ALog.e(TAG, "Cookie Manager setup failed!!!", null, th, new Object[0]);
            }
            f2003a = true;
        }
    }

    private static boolean d() {
        if (!f2003a && NetworkSdkSetting.getContext() != null) {
            setup(NetworkSdkSetting.getContext());
        }
        return f2003a;
    }

    public static synchronized void setCookie(String str, String str2) {
        if (NetworkConfigCenter.isCookieEnable()) {
            if (d() && f2005c) {
                try {
                    f2004b.setCookie(str, str2);
                    if (Build.VERSION.SDK_INT < 21) {
                        CookieSyncManager.getInstance().sync();
                    } else {
                        f2004b.flush();
                    }
                } catch (Throwable th) {
                    ALog.e(TAG, "set cookie failed.", null, th, "url", str, "cookies", str2);
                }
            }
        }
    }

    public static void setCookie(String str, Map<String, List<String>> map) {
        if (!NetworkConfigCenter.isCookieEnable() || str == null || map == null) {
            return;
        }
        try {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                if (key != null && (key.equalsIgnoreCase("Set-Cookie") || key.equalsIgnoreCase("Set-Cookie2"))) {
                    for (String str2 : entry.getValue()) {
                        setCookie(str, str2);
                        a(str2);
                    }
                }
            }
        } catch (Exception e2) {
            ALog.e(TAG, "set cookie failed", null, e2, "url", str, "\nheaders", map);
        }
    }

    public static synchronized String getCookie(String str) {
        String cookie = null;
        if (!NetworkConfigCenter.isCookieEnable()) {
            return null;
        }
        if (!d() || !f2005c) {
            return null;
        }
        try {
            cookie = f2004b.getCookie(str);
        } catch (Throwable th) {
            ALog.e(TAG, "get cookie failed. url=" + str, null, th, new Object[0]);
        }
        a(str, cookie);
        return cookie;
    }

    private static void e() {
        ThreadPoolExecutorFactory.submitCookieMonitor(new anetwork.channel.cookie.a());
    }

    private static void a(String str) {
        ThreadPoolExecutorFactory.submitCookieMonitor(new b(str));
    }

    private static void a(String str, String str2) {
        ThreadPoolExecutorFactory.submitCookieMonitor(new c(str, str2));
    }

    public static void setTargetMonitorCookieName(String str) {
        SharedPreferences sharedPreferences;
        if (str == null || (sharedPreferences = e) == null) {
            return;
        }
        sharedPreferences.edit().putString("networksdk_target_cookie_name", str).apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String f() {
        SharedPreferences sharedPreferences = e;
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString("networksdk_target_cookie_name", null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f2007a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        String f2008b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        String f2009c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        String f2010d;
        long e;

        a(String str) {
            this.f2007a = str;
            String string = CookieManager.e.getString("networksdk_cookie_monitor", null);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(string);
                if (!TextUtils.isEmpty(this.f2007a) && this.f2007a.equals(jSONObject.getString("cookieName"))) {
                    this.e = jSONObject.getLong("time");
                    if (System.currentTimeMillis() - this.e < 86400000) {
                        this.f2008b = jSONObject.getString("cookieText");
                        this.f2009c = jSONObject.getString("setCookie");
                        this.f2010d = jSONObject.getString("domain");
                    } else {
                        this.e = 0L;
                        CookieManager.e.edit().remove("networksdk_cookie_monitor").apply();
                    }
                }
            } catch (JSONException e) {
                ALog.e(CookieManager.TAG, "cookie json parse error.", null, e, new Object[0]);
            }
        }

        void a() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("cookieName", this.f2007a);
                jSONObject.put("cookieText", this.f2008b);
                jSONObject.put("setCookie", this.f2009c);
                this.e = System.currentTimeMillis();
                jSONObject.put("time", this.e);
                jSONObject.put("domain", this.f2010d);
                CookieManager.e.edit().putString("networksdk_cookie_monitor", jSONObject.toString()).apply();
            } catch (Exception e) {
                ALog.e(CookieManager.TAG, "cookie json save error.", null, e, new Object[0]);
            }
        }
    }
}

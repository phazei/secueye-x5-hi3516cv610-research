package com.taobao.accs.client;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import com.taobao.accs.ILoginInfo;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class GlobalClientInfo {
    public static final String AGOO_SERVICE_ID = "agooSend";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static Context f6289a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static String f6290b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static boolean f6291c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile GlobalClientInfo f6292d;
    private static Map<String, String> i = new ConcurrentHashMap();
    private ConcurrentHashMap<String, ILoginInfo> e;
    private ActivityManager f;
    private ConnectivityManager g;
    private PackageInfo h;
    private Map<String, AccsAbstractDataListener> j = new ConcurrentHashMap();

    static {
        i.put(AGOO_SERVICE_ID, "org.android.agoo.accs.AgooService");
        i.put(AgooConstants.AGOO_SERVICE_AGOOACK, "org.android.agoo.accs.AgooService");
        i.put("agooTokenReport", "org.android.agoo.accs.AgooService");
    }

    public static GlobalClientInfo getInstance(Context context) {
        if (f6292d == null) {
            synchronized (GlobalClientInfo.class) {
                if (f6292d == null) {
                    f6292d = new GlobalClientInfo(context);
                }
            }
        }
        return f6292d;
    }

    public static Context getContext() {
        return f6289a;
    }

    private GlobalClientInfo(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is null!!");
        }
        if (f6289a == null) {
            f6289a = context.getApplicationContext();
        }
        ThreadPoolExecutorFactory.execute(new c(this));
    }

    public ActivityManager getActivityManager() {
        if (this.f == null) {
            this.f = (ActivityManager) f6289a.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        }
        return this.f;
    }

    public ConnectivityManager getConnectivityManager() {
        if (this.g == null) {
            this.g = (ConnectivityManager) f6289a.getSystemService("connectivity");
        }
        return this.g;
    }

    public void setLoginInfoImpl(String str, ILoginInfo iLoginInfo) {
        if (this.e == null) {
            this.e = new ConcurrentHashMap<>(1);
        }
        if (iLoginInfo != null) {
            this.e.put(str, iLoginInfo);
        }
    }

    public void clearLoginInfoImpl() {
        this.e = null;
    }

    public String getSid(String str) {
        ILoginInfo iLoginInfo;
        ConcurrentHashMap<String, ILoginInfo> concurrentHashMap = this.e;
        if (concurrentHashMap == null || (iLoginInfo = concurrentHashMap.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getSid();
    }

    public String getUserId(String str) {
        ILoginInfo iLoginInfo;
        ConcurrentHashMap<String, ILoginInfo> concurrentHashMap = this.e;
        if (concurrentHashMap == null || (iLoginInfo = concurrentHashMap.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getUserId();
    }

    public String getNick(String str) {
        ILoginInfo iLoginInfo;
        ConcurrentHashMap<String, ILoginInfo> concurrentHashMap = this.e;
        if (concurrentHashMap == null || (iLoginInfo = concurrentHashMap.get(str)) == null) {
            return null;
        }
        return iLoginInfo.getNick();
    }

    public void registerService(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        i.put(str, str2);
    }

    public void unRegisterService(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        i.remove(str);
    }

    public String getService(String str) {
        return i.get(str);
    }

    public void registerListener(String str, AccsAbstractDataListener accsAbstractDataListener) {
        if (TextUtils.isEmpty(str) || accsAbstractDataListener == null) {
            return;
        }
        this.j.put(str, accsAbstractDataListener);
    }

    public void unregisterListener(String str) {
        this.j.remove(str);
    }

    public AccsAbstractDataListener getListener(String str) {
        return this.j.get(str);
    }

    public PackageInfo getPackageInfo() {
        try {
            if (this.h == null) {
                this.h = f6289a.getPackageManager().getPackageInfo(f6289a.getPackageName(), 0);
            }
        } catch (Throwable th) {
            ALog.e("GlobalClientInfo", "getPackageInfo", th, new Object[0]);
        }
        return this.h;
    }
}

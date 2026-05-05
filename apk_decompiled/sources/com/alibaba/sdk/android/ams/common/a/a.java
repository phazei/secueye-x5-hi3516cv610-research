package com.alibaba.sdk.android.ams.common.a;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static volatile Context f2828a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static volatile Application f2829b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static volatile boolean f2830c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static volatile String f2831d = null;
    static volatile String e = null;
    static volatile String f = null;
    static volatile String g = null;
    static volatile String h = "";

    public static Application a() {
        return f2829b;
    }

    public static String a(String str) {
        try {
            ApplicationInfo applicationInfo = f2828a.getPackageManager().getApplicationInfo(f2828a.getPackageName(), 128);
            if (applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey(str)) {
                return null;
            }
            return String.valueOf(applicationInfo.metaData.get(str));
        } catch (PackageManager.NameNotFoundException unused) {
            AmsLogger.getImportantLogger().e("Meta data name " + str + " not found!");
            return null;
        }
    }

    public static Context b() {
        return f2828a;
    }

    public static String c() {
        return h;
    }

    public static boolean d() {
        return f2830c;
    }

    public static String e() {
        return f2831d == null ? "mpush-api.aliyun.com" : f2831d;
    }

    public static String f() {
        return e == null ? "msgacs.cn-zhangjiakou.aliyuncs.com" : e;
    }

    public static String g() {
        return f == null ? "jmacs.cn-zhangjiakou.aliyuncs.com" : f;
    }

    public static String h() {
        return g;
    }

    public static boolean i() {
        return e().equals("mpush-api.aliyun.com");
    }

    public static String j() {
        return "https://" + e() + "/config";
    }

    public static SharedPreferences k() {
        return PreferenceManager.getDefaultSharedPreferences(f2828a);
    }

    public static String l() {
        return f2828a.getPackageName();
    }
}

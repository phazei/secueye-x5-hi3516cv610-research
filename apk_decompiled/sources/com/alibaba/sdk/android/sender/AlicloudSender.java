package com.alibaba.sdk.android.sender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.sdk.android.logger.ILog;
import com.alibaba.sdk.android.tbrest.SendService;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.common.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AlicloudSender {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3169a = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Map<String, SdkInfo> f3171c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Map<String, a> f3172d;
    private static SendService g;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final ExecutorService f3170b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private static final AtomicBoolean e = new AtomicBoolean(false);
    private static final AtomicBoolean f = new AtomicBoolean(false);
    private static final ILog h = SenderLog.getLogger(AlicloudSender.class);
    private static boolean i = false;

    @SuppressLint({"SimpleDateFormat"})
    private static final SimpleDateFormat j = new SimpleDateFormat("yyyyMMdd");

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f3176a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f3177b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f3178c;

        private a() {
            this.f3176a = -1;
            this.f3177b = "";
            this.f3178c = "";
        }
    }

    private static void a(Application application) {
        if (!f.compareAndSet(false, true) || Build.VERSION.SDK_INT < 14) {
            return;
        }
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: com.alibaba.sdk.android.sender.AlicloudSender.1
            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity2, Bundle bundle) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityDestroyed(Activity activity2) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity2) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityResumed(Activity activity2) {
                if (AlicloudSender.f3171c == null || AlicloudSender.f3171c.isEmpty()) {
                    return;
                }
                Iterator it = AlicloudSender.f3171c.values().iterator();
                while (it.hasNext()) {
                    AlicloudSender.b(activity2.getApplicationContext(), (SdkInfo) it.next());
                }
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStarted(Activity activity2) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity2) {
            }
        });
    }

    private static void a(Context context) {
        if (e.compareAndSet(false, true)) {
            f3171c = new ConcurrentHashMap();
            f3172d = c(context);
            g = new SendService();
            g.openHttp = Boolean.valueOf(i);
            g.init(context, "24527540@android", "24527540", b(context), null, null);
            g.appSecret = "56fc10fbe8c6ae7d0d895f49c4fb6838";
        }
    }

    private static void a(Context context, Map<String, a> map) {
        SharedPreferences.Editor editorRemove;
        if (map == null || map.isEmpty()) {
            editorRemove = context.getSharedPreferences("sp_emas_info", 0).edit().remove("emas_sdk_info");
        } else {
            JSONArray jSONArray = new JSONArray();
            for (String str : map.keySet()) {
                a aVar = map.get(str);
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("id", str);
                    jSONObject.put("version", aVar.f3178c);
                    jSONObject.put("time", aVar.f3177b);
                    jSONObject.put("statu", aVar.f3176a);
                    jSONArray.put(jSONObject);
                } catch (Exception unused) {
                }
            }
            editorRemove = context.getSharedPreferences("sp_emas_info", 0).edit().putString("emas_sdk_info", jSONArray.toString());
        }
        editorRemove.apply();
    }

    public static void asyncSend(Application application, SdkInfo sdkInfo) {
        if (application == null) {
            h.d("asyncSend failed. application is null. ");
            return;
        }
        if (sdkInfo == null) {
            h.d("asyncSend failed. sdk info is null. ");
            return;
        }
        String strA = sdkInfo.a();
        if (TextUtils.isEmpty(strA)) {
            h.d("asyncSend failed. sdk id is empty. ");
            return;
        }
        if (TextUtils.isEmpty(sdkInfo.b())) {
            h.d("asyncSend failed. sdk version is empty. ");
            return;
        }
        a(application.getApplicationContext());
        a(application);
        f3171c.put(strA, sdkInfo);
        b(application.getApplicationContext(), sdkInfo);
    }

    @Deprecated
    public static void asyncSend(Context context, SdkInfo sdkInfo) {
        if (context == null) {
            h.d("asyncSend failed. context is null. ");
            return;
        }
        if (sdkInfo == null) {
            h.d("asyncSend failed. sdk info is null. ");
            return;
        }
        String strA = sdkInfo.a();
        if (TextUtils.isEmpty(strA)) {
            h.d("asyncSend failed. sdk id is empty. ");
        } else {
            if (TextUtils.isEmpty(sdkInfo.b())) {
                h.d("asyncSend failed. sdk version is empty. ");
                return;
            }
            a(context.getApplicationContext());
            f3171c.put(strA, sdkInfo);
            b(context.getApplicationContext(), sdkInfo);
        }
    }

    private static String b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(final Context context, final SdkInfo sdkInfo) {
        final String str = j.format(new Date(System.currentTimeMillis()));
        try {
            a aVar = f3172d.get(sdkInfo.a());
            if (aVar == null || !TextUtils.equals(str, aVar.f3177b) || !TextUtils.equals(sdkInfo.b(), aVar.f3178c) || aVar.f3176a != 0) {
                f3170b.execute(new Runnable() { // from class: com.alibaba.sdk.android.sender.AlicloudSender.2
                    @Override // java.lang.Runnable
                    public void run() {
                        AlicloudSender.b(context, sdkInfo, str);
                    }
                });
                return;
            }
            h.d(sdkInfo.a() + " " + sdkInfo.b() + " send abort send. ");
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(Context context, SdkInfo sdkInfo, String str) {
        a aVar = f3172d.get(sdkInfo.a());
        if (aVar == null) {
            aVar = new a();
            f3172d.put(sdkInfo.a(), aVar);
        }
        HashMap map = new HashMap();
        map.put("sdkId", sdkInfo.a());
        map.put(Constants.KEY_PACKAGE_NAME, context.getPackageName());
        map.put("sdkVersion", sdkInfo.b());
        map.put("kVersion", AlinkConstants.PROVISION_DEVICE_PIDTOPK_VERSION);
        if (!TextUtils.isEmpty(sdkInfo.c())) {
            map.put("appKey", sdkInfo.c());
        }
        if (sdkInfo.f3179a != null) {
            map.putAll(sdkInfo.f3179a);
        }
        map.put("_aliyun_biz_id", "emas-active");
        h.d(sdkInfo.a() + " " + sdkInfo.b() + " start send. ");
        boolean zBooleanValue = g.sendRequest("adash-emas.cn-hangzhou.aliyuncs.com", System.currentTimeMillis(), f3169a, 19999, sdkInfo.a() + "_biz_active", null, null, map).booleanValue();
        ILog iLog = h;
        StringBuilder sb = new StringBuilder();
        sb.append(sdkInfo.a());
        sb.append(" ");
        sb.append(sdkInfo.b());
        sb.append(" send ");
        sb.append(zBooleanValue ? "success. " : "failed. ");
        iLog.d(sb.toString());
        aVar.f3177b = str;
        aVar.f3178c = sdkInfo.b();
        aVar.f3176a = zBooleanValue ? 0 : -1;
        a(context, f3172d);
    }

    private static Map<String, a> c(Context context) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        String string = context.getSharedPreferences("sp_emas_info", 0).getString("emas_sdk_info", "");
        if (!TextUtils.isEmpty(string)) {
            try {
                JSONArray jSONArray = new JSONArray(string);
                if (jSONArray.length() > 0) {
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i2);
                        String string2 = jSONObject.getString("id");
                        a aVar = new a();
                        aVar.f3177b = jSONObject.getString("time");
                        aVar.f3176a = jSONObject.getInt("statu");
                        aVar.f3178c = jSONObject.getString("version");
                        concurrentHashMap.put(string2, aVar);
                    }
                }
            } catch (Exception unused) {
            }
        }
        return concurrentHashMap;
    }

    public static void openHttp() {
        i = true;
    }
}

package com.alibaba.sdk.android.utils;

import android.app.Application;
import android.util.Log;
import com.ut.mini.IUTApplication;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.crashhandler.IUTCrashCaughtListner;
import java.util.Map;

/* JADX INFO: compiled from: DataTracker.java */
/* JADX INFO: loaded from: classes.dex */
public class c {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f3212b = true;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Map<String, String> f3213d;

    public void a(Application application, Map<String, String> map) {
        this.f3213d = map;
        this.f3212b = b();
        if (!this.f3212b) {
            Log.e("Utils:DataTracker", "init failed due to ut not exsits");
            return;
        }
        try {
            UTAnalytics.getInstance().setAppApplicationInstance4sdk(application, new IUTApplication() { // from class: com.alibaba.sdk.android.utils.c.1
                public String getUTAppVersion() {
                    return null;
                }

                public String getUTChannel() {
                    return null;
                }

                public IUTCrashCaughtListner getUTCrashCraughtListener() {
                    return null;
                }

                public boolean isAliyunOsSystem() {
                    return false;
                }

                public boolean isUTCrashHandlerDisable() {
                    return true;
                }

                public IUTRequestAuthentication getUTRequestAuthInstance() {
                    return new UTBaseRequestAuthentication("24527540", "56fc10fbe8c6ae7d0d895f49c4fb6838");
                }

                public boolean isUTLogEnable() {
                    return d.c();
                }
            });
        } catch (Throwable th) {
            Log.e("Utils:DataTracker", "init data tracker failed.", th);
        }
    }

    public void sendCustomHit(String str, long j, Map<String, String> map) {
        if (!this.f3212b) {
            Log.e("Utils:DataTracker", "send custom hit failed due to ut not exists");
            return;
        }
        try {
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str);
            uTCustomHitBuilder.setDurationOnEvent(j);
            uTCustomHitBuilder.setProperties(map);
            uTCustomHitBuilder.setProperties(this.f3213d);
            UTAnalytics.getInstance().getTrackerByAppkey("24527540").send(uTCustomHitBuilder.build());
        } catch (Throwable th) {
            Log.e("Utils:DataTracker", "send custom hit failed", th);
        }
    }

    private boolean b() {
        try {
            Class.forName("com.ut.mini.UTAnalytics");
            return true;
        } catch (Throwable th) {
            Log.e("Utils:DataTracker", "ut not exist", th);
            return false;
        }
    }
}

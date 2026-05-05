package com.alibaba.sdk.android.openaccount.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class ActivityHelper {

    /* JADX INFO: renamed from: activity, reason: collision with root package name */
    private static WeakReference<Activity> f2929activity;
    private static volatile Application.ActivityLifecycleCallbacks callback;

    public static Activity getCurrentActivity() {
        WeakReference<Activity> weakReference = f2929activity;
        Activity activity2 = weakReference == null ? null : weakReference.get();
        return activity2 == null ? getCurrentActivityFromActivityThread() : activity2;
    }

    @TargetApi(14)
    public static void register(Application application) {
        if (application == null) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "null application for activity lifecycle registeration");
        } else if (Build.VERSION.SDK_INT >= 14 && callback == null) {
            synchronized (ActivityHelper.class) {
                callback = new Application.ActivityLifecycleCallbacks() { // from class: com.alibaba.sdk.android.openaccount.util.ActivityHelper.1
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
                    public void onActivitySaveInstanceState(Activity activity2, Bundle bundle) {
                    }

                    @Override // android.app.Application.ActivityLifecycleCallbacks
                    public void onActivityStarted(Activity activity2) {
                    }

                    @Override // android.app.Application.ActivityLifecycleCallbacks
                    public void onActivityStopped(Activity activity2) {
                    }

                    @Override // android.app.Application.ActivityLifecycleCallbacks
                    public void onActivityResumed(Activity activity2) {
                        WeakReference unused = ActivityHelper.f2929activity = new WeakReference(activity2);
                    }
                };
                application.registerActivityLifecycleCallbacks(callback);
            }
        }
    }

    @TargetApi(14)
    public static void unregister(Application application) {
        if (application == null) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "null application for activity lifecycle registeration");
        } else {
            if (Build.VERSION.SDK_INT < 14 || callback == null) {
                return;
            }
            synchronized (ActivityHelper.class) {
                application.unregisterActivityLifecycleCallbacks(callback);
                callback = null;
            }
        }
    }

    private static Activity getCurrentActivityFromActivityThread() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mActivities");
            declaredField.setAccessible(true);
        } catch (Throwable unused) {
        }
        for (Object obj : ((Map) declaredField.get(objInvoke)).values()) {
            Class<?> cls2 = obj.getClass();
            Field declaredField2 = cls2.getDeclaredField("paused");
            declaredField2.setAccessible(true);
            if (!declaredField2.getBoolean(obj)) {
                Field declaredField3 = cls2.getDeclaredField(AgooConstants.OPEN_ACTIIVTY_NAME);
                declaredField3.setAccessible(true);
                return (Activity) declaredField3.get(obj);
            }
            return null;
        }
        return null;
    }
}

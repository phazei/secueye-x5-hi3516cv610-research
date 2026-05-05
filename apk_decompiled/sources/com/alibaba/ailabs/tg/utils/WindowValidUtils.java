package com.alibaba.ailabs.tg.utils;

import android.app.Activity;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
public final class WindowValidUtils {
    public static boolean isWindowEffective(Activity activity2) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return false;
        }
        if (activity2 != null) {
            return (activity2.isFinishing() || activity2.isDestroyed()) ? false : true;
        }
        return true;
    }

    private static boolean isTokenValid(View view2) {
        IBinder windowToken;
        if (view2 == null || (windowToken = view2.getWindowToken()) == null) {
            return false;
        }
        try {
            if (windowToken.isBinderAlive()) {
                return windowToken.pingBinder();
            }
            return false;
        } catch (Throwable th) {
            LogUtils.e(th.toString());
            th.printStackTrace();
            return false;
        }
    }

    public static boolean isActivityIllegal(Activity activity2) {
        return activity2 == null || activity2.isDestroyed();
    }
}

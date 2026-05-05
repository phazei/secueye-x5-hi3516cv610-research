package com.alibaba.sdk.android.openaccount.ui.ui.util;

import android.app.Activity;
import android.os.Build;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* JADX INFO: loaded from: classes.dex */
public class StatusBarHelper {
    public static final int ANDROID_M = 3;
    public static final int FLYME = 2;
    public static final int MIUI = 1;
    public static final int OTHER = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SystemType {
    }

    public static int setStatusBarMode(Activity activity2, boolean z) {
        if (Build.VERSION.SDK_INT > 19) {
            if (new MIUIHelper().setStatusBarLightMode(activity2, z)) {
                return 1;
            }
            if (new FlymeHelper().setStatusBarLightMode(activity2, z)) {
                return 2;
            }
            if (new AndroidMHelper().setStatusBarLightMode(activity2, z)) {
                return 3;
            }
        }
        return 0;
    }

    public static void setLightMode(Activity activity2, int i) {
        setStatusBarMode(activity2, i, true);
    }

    public static void setDarkMode(Activity activity2, int i) {
        setStatusBarMode(activity2, i, false);
    }

    private static void setStatusBarMode(Activity activity2, int i, boolean z) {
        if (i == 1) {
            new MIUIHelper().setStatusBarLightMode(activity2, z);
        } else if (i == 2) {
            new FlymeHelper().setStatusBarLightMode(activity2, z);
        } else if (i == 3) {
            new AndroidMHelper().setStatusBarLightMode(activity2, z);
        }
    }
}

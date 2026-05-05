package com.alibaba.ailabs.tg.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import anet.channel.strategy.dispatch.DispatchConstants;

/* JADX INFO: loaded from: classes.dex */
public final class ScreenUtils {
    private ScreenUtils() {
    }

    public static int getScreenWidth(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        return point.x;
    }

    public static int getScreenHeight(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    public static float getScreenDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenDensityDpi(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static void setFullScreen(@NonNull Activity activity2) {
        activity2.getWindow().addFlags(1536);
    }

    public static void setLandscape(@NonNull Activity activity2) {
        activity2.setRequestedOrientation(0);
    }

    public static void setPortrait(@NonNull Activity activity2) {
        activity2.setRequestedOrientation(1);
    }

    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static boolean isPortrait(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    public static int getScreenRotation(@NonNull Activity activity2) {
        switch (activity2.getWindowManager().getDefaultDisplay().getRotation()) {
        }
        return 0;
    }

    public static Bitmap screenShot(@NonNull Activity activity2) {
        return screenShot(activity2, false);
    }

    public static Bitmap screenShot(@NonNull Activity activity2, boolean z) {
        Bitmap bitmapCreateBitmap;
        View decorView = activity2.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity2.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        if (z) {
            Resources resources = activity2.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", DispatchConstants.ANDROID));
            bitmapCreateBitmap = Bitmap.createBitmap(drawingCache, 0, dimensionPixelSize, displayMetrics.widthPixels, displayMetrics.heightPixels - dimensionPixelSize);
        } else {
            bitmapCreateBitmap = Bitmap.createBitmap(drawingCache, 0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        decorView.destroyDrawingCache();
        return bitmapCreateBitmap;
    }

    public static boolean isScreenLock(@NonNull Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService("keyguard");
        return keyguardManager != null && keyguardManager.inKeyguardRestrictedInputMode();
    }

    public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }
}

package tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes4.dex */
public final class DensityUtils {
    private DensityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static DisplayMetrics getDisplayMetrics() {
        return ResUtils.getResources().getDisplayMetrics();
    }

    public static int dp2px(float f) {
        return (int) ((f * ResUtils.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(float f) {
        return (int) ((f / ResUtils.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2sp(float f) {
        return (int) ((f / ResUtils.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int px2sp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int sp2px(float f) {
        return (int) ((f * ResUtils.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int getScreenDpi() {
        return getDisplayMetrics().densityDpi;
    }

    public static int getRealDpi(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (((displayMetrics.xdpi + displayMetrics.ydpi) / 2.0f) + 0.5f);
    }

    public static Point getAppSize(Activity activity2, boolean z) {
        return getDisplaySize(activity2, z);
    }

    public static Point getDisplaySize(Context context, boolean z) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService("window");
        }
        if (windowManager == null) {
            return null;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (z) {
            defaultDisplay.getRealSize(point);
            return point;
        }
        defaultDisplay.getSize(point);
        return point;
    }

    public static DisplayMetrics getAppMetrics(Activity activity2, boolean z) {
        return getDisplayMetrics(activity2, z);
    }

    public static DisplayMetrics getDisplayMetrics(Context context, boolean z) {
        WindowManager windowManager;
        if (context instanceof Activity) {
            windowManager = ((Activity) context).getWindowManager();
        } else {
            windowManager = (WindowManager) context.getSystemService("window");
        }
        if (windowManager == null) {
            return null;
        }
        Display defaultDisplay = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (z) {
            defaultDisplay.getRealMetrics(displayMetrics);
            return displayMetrics;
        }
        defaultDisplay.getMetrics(displayMetrics);
        return displayMetrics;
    }
}

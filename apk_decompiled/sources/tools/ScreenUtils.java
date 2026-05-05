package tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/* JADX INFO: loaded from: classes4.dex */
public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getStatusHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap snapShotWithStatusBar(Activity activity2) {
        View decorView = activity2.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(decorView.getDrawingCache(), 0, 0, getScreenWidth(activity2), getScreenHeight(activity2));
        decorView.destroyDrawingCache();
        return bitmapCreateBitmap;
    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity2) {
        View decorView = activity2.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap drawingCache = decorView.getDrawingCache();
        Rect rect = new Rect();
        activity2.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i = rect.top;
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawingCache, 0, i, getScreenWidth(activity2), getScreenHeight(activity2) - i);
        decorView.destroyDrawingCache();
        return bitmapCreateBitmap;
    }
}

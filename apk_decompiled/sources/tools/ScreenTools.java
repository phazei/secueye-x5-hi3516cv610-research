package tools;

import android.content.Context;
import android.util.TypedValue;

/* JADX INFO: loaded from: classes4.dex */
public class ScreenTools {
    public static double convertDp2Px(Context context, float f) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static double convertPx2Dp(Context context, float f) {
        return f / context.getResources().getDisplayMetrics().density;
    }

    public static double getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static double getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}

package tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.annotation.AnimRes;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

/* JADX INFO: loaded from: classes4.dex */
public final class ResUtils {
    private ResUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Resources getResources() {
        return MyApplication.getContext().getResources();
    }

    public static String getString(@StringRes int i) {
        return getResources().getString(i);
    }

    public static Drawable getDrawable(@DrawableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return MyApplication.getContext().getDrawable(i);
        }
        return getResources().getDrawable(i);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getDrawable(i);
        }
        return AppCompatResources.getDrawable(context, i);
    }

    public static Drawable getVectorDrawable(Context context, @DrawableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getDrawable(i);
        }
        return AppCompatResources.getDrawable(context, i);
    }

    public static Drawable getDrawableAttrRes(Context context, TypedArray typedArray, @StyleableRes int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return typedArray.getDrawable(i);
        }
        int resourceId = typedArray.getResourceId(i, -1);
        if (resourceId != -1) {
            return AppCompatResources.getDrawable(context, resourceId);
        }
        return null;
    }

    public static float getDimens(@DimenRes int i) {
        return getResources().getDimension(i);
    }

    public static int getColor(@ColorRes int i) {
        return getResources().getColor(i);
    }

    public static ColorStateList getColors(@ColorRes int i) {
        return getResources().getColorStateList(i);
    }

    public static int getDimensionPixelOffset(@DimenRes int i) {
        return getResources().getDimensionPixelOffset(i);
    }

    public static int getDimensionPixelSize(@DimenRes int i) {
        return getResources().getDimensionPixelSize(i);
    }

    public static String[] getStringArray(@ArrayRes int i) {
        return getResources().getStringArray(i);
    }

    public static Drawable[] getDrawableArray(Context context, @ArrayRes int i) {
        TypedArray typedArrayObtainTypedArray = getResources().obtainTypedArray(i);
        Drawable[] drawableArr = new Drawable[typedArrayObtainTypedArray.length()];
        for (int i2 = 0; i2 < typedArrayObtainTypedArray.length(); i2++) {
            int resourceId = typedArrayObtainTypedArray.getResourceId(i2, 0);
            if (resourceId != 0) {
                drawableArr[i2] = ContextCompat.getDrawable(context, resourceId);
            }
        }
        typedArrayObtainTypedArray.recycle();
        return drawableArr;
    }

    public static int[] getIntArray(@ArrayRes int i) {
        return getResources().getIntArray(i);
    }

    public static Animation getAnim(@AnimRes int i) {
        return AnimationUtils.loadAnimation(MyApplication.getContext(), i);
    }

    public static boolean isRtl() {
        return Build.VERSION.SDK_INT >= 17 && getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public static int darker(int i, float f) {
        return Color.argb(Color.alpha(i), Math.max((int) (Color.red(i) * f), 0), Math.max((int) (Color.green(i) * f), 0), Math.max((int) (Color.blue(i) * f), 0));
    }

    public static int lighter(int i, float f) {
        float f2 = 1.0f - f;
        return Color.argb(Color.alpha(i), (int) ((((Color.red(i) * f2) / 255.0f) + f) * 255.0f), (int) ((((Color.green(i) * f2) / 255.0f) + f) * 255.0f), (int) ((((Color.blue(i) * f2) / 255.0f) + f) * 255.0f));
    }
}

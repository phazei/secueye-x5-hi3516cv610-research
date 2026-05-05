package tools;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes4.dex */
public class ActivityCore {
    public static boolean isTranslucentOrFloating(Activity activity2) {
        boolean z = false;
        try {
            TypedArray typedArrayObtainStyledAttributes = activity2.obtainStyledAttributes((int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null));
            Method method = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            method.setAccessible(true);
            boolean zBooleanValue = ((Boolean) method.invoke(null, typedArrayObtainStyledAttributes)).booleanValue();
            try {
                method.setAccessible(false);
                return zBooleanValue;
            } catch (Exception e) {
                z = zBooleanValue;
                e = e;
                e.printStackTrace();
                return z;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static boolean fixOrientation(Activity activity2) {
        try {
            Field declaredField = Activity.class.getDeclaredField("mActivityInfo");
            declaredField.setAccessible(true);
            ((ActivityInfo) declaredField.get(activity2)).screenOrientation = -1;
            declaredField.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

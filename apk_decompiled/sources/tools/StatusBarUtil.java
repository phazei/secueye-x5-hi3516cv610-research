package tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes4.dex */
public class StatusBarUtil {
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity2) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity2.getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().setFlags(67108864, 67108864);
        }
    }

    public static void setStatusBarColor(Activity activity2, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity2.getWindow().setStatusBarColor(activity2.getResources().getColor(i));
        } else if (Build.VERSION.SDK_INT >= 19) {
            setTranslucentStatus(activity2);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(activity2);
            systemBarTintManager.setStatusBarTintEnabled(true);
            systemBarTintManager.setStatusBarTintResource(i);
        }
    }

    public static void setStatusBarMode(Activity activity2, boolean z, int i) {
        if (!z) {
            setStatusBarColor(activity2, i);
            return;
        }
        setStatusBarColor(activity2, i);
        if (Build.VERSION.SDK_INT >= 19) {
            if (OSUtil.isMIUI()) {
                setMIUIStatusBarTextMode(activity2, z);
                return;
            }
            if (OSUtil.isFlyme()) {
                setFlymeStatusBarTextMode(activity2, z);
            } else if (Build.VERSION.SDK_INT >= 23) {
                Window window = activity2.getWindow();
                window.addFlags(Integer.MIN_VALUE);
                window.clearFlags(67108864);
                window.getDecorView().setSystemUiVisibility(8192);
            }
        }
    }

    public static boolean setFlymeStatusBarTextMode(Activity activity2, boolean z) {
        Window window = activity2.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                window.setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    public static boolean setMIUIStatusBarTextMode(Activity activity2, boolean z) {
        Window window = activity2.getWindow();
        if (window == null) {
            return false;
        }
        Class<?> cls = window.getClass();
        try {
            Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
            Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            if (z) {
                method.invoke(window, Integer.valueOf(i), Integer.valueOf(i));
            } else {
                method.invoke(window, 0, Integer.valueOf(i));
            }
            try {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (z) {
                        activity2.getWindow().getDecorView().setSystemUiVisibility(9216);
                    } else {
                        activity2.getWindow().getDecorView().setSystemUiVisibility(0);
                    }
                }
                return true;
            } catch (Exception unused) {
                return true;
            }
        } catch (Exception unused2) {
            return false;
        }
    }

    @TargetApi(19)
    public static void transparencyBar(Activity activity2) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity2.getWindow();
            window.clearFlags(67108864);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            window.getDecorView().setSystemUiVisibility(1280);
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity2.getWindow().setFlags(67108864, 67108864);
        }
    }

    public static void setLightStatusBar(Activity activity2, boolean z) {
        if (Build.VERSION.SDK_INT >= 19) {
            switch (OSUtil.getLightStatusBarAvailableRomType()) {
                case 1:
                    MIUISetStatusBarLightMode(activity2, z);
                    break;
                case 2:
                    setFlymeLightStatusBar(activity2, z);
                    break;
                case 3:
                    setAndroidNativeLightStatusBar(activity2, z);
                    break;
            }
        }
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity2, boolean z) {
        Window window = activity2.getWindow();
        if (window == null) {
            return false;
        }
        Class<?> cls = window.getClass();
        try {
            Class<?> cls2 = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            int i = cls2.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE").getInt(cls2);
            Method method = cls.getMethod("setExtraFlags", Integer.TYPE, Integer.TYPE);
            if (z) {
                method.invoke(window, Integer.valueOf(i), Integer.valueOf(i));
            } else {
                method.invoke(window, 0, Integer.valueOf(i));
            }
            try {
                if (Build.VERSION.SDK_INT >= 23 && OSUtil.isMiUIV7OrAbove()) {
                    if (z) {
                        activity2.getWindow().getDecorView().setSystemUiVisibility(9216);
                    } else {
                        activity2.getWindow().getDecorView().setSystemUiVisibility(1280);
                    }
                }
                return true;
            } catch (Exception unused) {
                return true;
            }
        } catch (Exception unused2) {
            return false;
        }
    }

    private static boolean setFlymeLightStatusBar(Activity activity2, boolean z) {
        if (activity2 != null) {
            try {
                WindowManager.LayoutParams attributes = activity2.getWindow().getAttributes();
                Field declaredField = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field declaredField2 = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                declaredField.setAccessible(true);
                declaredField2.setAccessible(true);
                int i = declaredField.getInt(null);
                int i2 = declaredField2.getInt(attributes);
                declaredField2.setInt(attributes, z ? i2 | i : (~i) & i2);
                activity2.getWindow().setAttributes(attributes);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private static void setAndroidNativeLightStatusBar(Activity activity2, boolean z) {
        View decorView = activity2.getWindow().getDecorView();
        if (z) {
            decorView.setSystemUiVisibility(9216);
        } else {
            decorView.setSystemUiVisibility(1280);
        }
    }
}

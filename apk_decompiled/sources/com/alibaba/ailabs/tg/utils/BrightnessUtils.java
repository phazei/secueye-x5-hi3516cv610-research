package com.alibaba.ailabs.tg.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class BrightnessUtils {
    private BrightnessUtils() {
    }

    public static boolean isAutoBrightnessEnabled(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "screen_brightness_mode") == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setAutoBrightnessEnabled(Context context, boolean z) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(context)) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(268435456);
            context.startActivity(intent);
            return false;
        }
        return Settings.System.putInt(context.getContentResolver(), "screen_brightness_mode", z ? 1 : 0);
    }

    public static int getBrightness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean setBrightness(Context context, @IntRange(from = 0, to = 255) int i) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(context)) {
            Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            intent.addFlags(268435456);
            context.startActivity(intent);
            return false;
        }
        ContentResolver contentResolver = context.getContentResolver();
        boolean zPutInt = Settings.System.putInt(contentResolver, "screen_brightness", i);
        contentResolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
        return zPutInt;
    }

    public static void setWindowBrightness(@NonNull Window window, @IntRange(from = 0, to = 255) int i) {
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.screenBrightness = i / 255.0f;
        window.setAttributes(attributes);
    }

    public static int getWindowBrightness(Context context, Window window) {
        float f = window.getAttributes().screenBrightness;
        return f < 0.0f ? getBrightness(context) : (int) (f * 255.0f);
    }
}

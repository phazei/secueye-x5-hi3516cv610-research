package com.alibaba.ailabs.tg.utils;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.RequiresApi;
import anet.channel.strategy.dispatch.DispatchConstants;
import tools.LocationUtil;

/* JADX INFO: loaded from: classes.dex */
public class NavigationBarUtil {
    @RequiresApi(api = 21)
    public static int getNavigationBarHeightIfRoom(Context context) {
        if (navigationGestureEnabled(context)) {
            return 0;
        }
        return getCurrentNavigationBarHeight((Activity) context);
    }

    private static boolean navigationGestureEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0) != 0;
    }

    private static String getDeviceInfo() {
        String str = Build.BRAND;
        return (TextUtils.isEmpty(str) || str.equalsIgnoreCase(LocationUtil.MANUFACTURER_HUAWEI)) ? "navigationbar_is_min" : str.equalsIgnoreCase("XIAOMI") ? "force_fsg_nav_bar" : (str.equalsIgnoreCase("VIVO") || str.equalsIgnoreCase(LocationUtil.MANUFACTURER_OPPO)) ? "navigation_gesture_on" : "navigationbar_is_min";
    }

    @RequiresApi(api = 21)
    private static int getCurrentNavigationBarHeight(Activity activity2) {
        if (isNavigationBarShown(activity2)) {
            return getNavigationBarHeight(activity2);
        }
        return 0;
    }

    @RequiresApi(api = 21)
    private static boolean isNavigationBarShown(Activity activity2) {
        int visibility;
        View viewFindViewById = activity2.findViewById(R.id.navigationBarBackground);
        return (viewFindViewById == null || (visibility = viewFindViewById.getVisibility()) == 8 || visibility == 4) ? false : true;
    }

    public static int getNavigationBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", DispatchConstants.ANDROID);
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }
}

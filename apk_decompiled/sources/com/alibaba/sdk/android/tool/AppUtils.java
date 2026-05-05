package com.alibaba.sdk.android.tool;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes.dex */
public class AppUtils {
    public static Map<String, Object> getAppAllMeta(Context context) {
        Set<String> setKeySet;
        if (context == null) {
            return null;
        }
        HashMap map = new HashMap();
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null && (setKeySet = applicationInfo.metaData.keySet()) != null) {
                for (String str : setKeySet) {
                    map.put(str, applicationInfo.metaData.get(str));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Object> getAppMetaData(Context context, List<String> list) {
        if (context == null) {
            return null;
        }
        HashMap map = new HashMap();
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                for (String str : list) {
                    if (applicationInfo.metaData.containsKey(str)) {
                        map.put(str, applicationInfo.metaData.get(str));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int getAppVersionCode(Context context) {
        if (context == null) {
            return -1;
        }
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getAppVersionName(Context context) {
        if (context == null) {
            return "unknown";
        }
        try {
            return context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public static String getGMT8Time(long j) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(j));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

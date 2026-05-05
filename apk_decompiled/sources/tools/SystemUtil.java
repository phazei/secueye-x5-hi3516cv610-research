package tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.aliyun.iot.aep.sdk.framework.region.CountryManager;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes4.dex */
public class SystemUtil {
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static int getSdkAPILevel() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isZhJianTi() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage().toUpperCase().equals("ZH") && locale.getCountry().toUpperCase().equals(CountryManager.COUNTRY_CHINA_ABBR);
    }

    public static boolean isEmail(String str) {
        if (TextUtils.isEmpty("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$")) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");
    }

    public static boolean isPhone(String str) {
        if (TextUtils.isEmpty("^[0-9]*$")) {
            return false;
        }
        return str.matches("^[0-9]*$");
    }

    public static String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    public static Intent getIntent(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=com.seculink.app");
        Log.e("fang", "getIntent: " + sb.toString());
        return new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
    }

    public static boolean judge(Context context, Intent intent) {
        List<ResolveInfo> listQueryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 64);
        return listQueryIntentActivities == null || listQueryIntentActivities.size() <= 0;
    }

    public static void toMarket(Activity activity2) {
        Intent intent = getIntent(activity2);
        boolean zJudge = judge(activity2, intent);
        Log.e("fang", "b: " + zJudge);
        if (!zJudge) {
            try {
                activity2.startActivity(intent);
                return;
            } catch (ActivityNotFoundException unused) {
                Log.e("fang", "ActivityNotFoundException: Constants.ERROR_NO_MARKET");
                return;
            }
        }
        Log.e("fang", "ActivityNotFoundException: Constants.ERROR_NO_MARKET");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:38:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void getAutostartSettingIntent(android.app.Activity r4) {
        /*
            Method dump skipped, instruction units count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.SystemUtil.getAutostartSettingIntent(android.app.Activity):void");
    }
}

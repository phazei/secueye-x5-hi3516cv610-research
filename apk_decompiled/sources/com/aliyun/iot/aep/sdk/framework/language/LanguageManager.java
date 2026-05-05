package com.aliyun.iot.aep.sdk.framework.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.appcompat.view.ContextThemeWrapper;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.sdk.bone.plugins.config.BoneConfig;
import com.aliyun.iot.aep.oa.OALanguageHelper;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.framework.R;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKManager;
import com.aliyun.iot.aep.sdk.init.PushManagerHelper;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.xiaomi.mipush.sdk.Constants;
import java.util.Locale;
import sdk.EnvConfigure;

/* JADX INFO: loaded from: classes2.dex */
public class LanguageManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f4682a = false;

    public static void initAppLanguage() {
        f4682a = true;
        a();
    }

    public static String makeLanguageString(Locale locale) {
        return locale.getLanguage() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + locale.getCountry();
    }

    public static String[] loadLanguageInfo(String str) {
        String[] strArrSplit = str.split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        return strArrSplit.length == 1 ? str.split(OpenAccountUIConstants.UNDER_LINE) : strArrSplit;
    }

    public static String getDefaultLanguage() {
        return makeLanguageString(GetSysLocale());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.Locale GetAppLocal() {
        /*
            Method dump skipped, instruction units count: 282
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.aep.sdk.framework.language.LanguageManager.GetAppLocal():java.util.Locale");
    }

    public static Locale GetSysLocale() {
        Locale locale;
        Locale locale2;
        if (Build.VERSION.SDK_INT >= 24) {
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            locale = Locale.getDefault();
        }
        switch (locale.getLanguage().toLowerCase()) {
            case "zh":
                locale2 = Locale.SIMPLIFIED_CHINESE;
                break;
            case "en":
                locale2 = Locale.US;
                break;
            case "fr":
                locale2 = Locale.FRANCE;
                break;
            case "de":
                locale2 = Locale.GERMANY;
                break;
            case "ja":
                locale2 = Locale.JAPAN;
                break;
            case "ko":
                locale2 = Locale.KOREA;
                break;
            case "es":
                locale2 = new Locale("es", "ES");
                break;
            case "ru":
                locale2 = new Locale("ru", "RU");
                break;
            case "hi":
                locale2 = new Locale("hi", "IN");
                break;
            case "it":
                locale2 = new Locale("it", "IT");
                break;
            case "pt":
                locale2 = new Locale("pt", "PT");
                break;
            case "nl":
                locale2 = new Locale("nl", "NL");
                break;
            case "pl":
                locale2 = new Locale("pl", "PL");
                break;
            default:
                locale2 = Locale.US;
                break;
        }
        ALog.d("LanguageManager", "当前本地语言为：" + locale2.getLanguage());
        return locale2;
    }

    private static boolean a(String str) {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            strArrSplit = str.split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            if (strArrSplit.length == 1) {
                strArrSplit = str.split(OpenAccountUIConstants.UNDER_LINE);
            }
        } catch (Exception unused) {
        }
        return strArrSplit.length == 2;
    }

    public static void setLanguage(String str) {
        try {
            if (!a(str)) {
                ALog.e("LanguageManager", "set wrong language, the format is like 'zh-CN' or 'en-US'");
                return;
            }
            String[] strArrLoadLanguageInfo = loadLanguageInfo(str);
            Locale locale = new Locale(strArrLoadLanguageInfo[0], strArrLoadLanguageInfo[1]);
            try {
                try {
                    if (SDKManager.isOAAvailable()) {
                        OALanguageHelper.setLanguageCode(locale);
                    } else {
                        Resources resources = AApplication.getInstance().getResources();
                        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                        Configuration configuration = resources.getConfiguration();
                        if (Build.VERSION.SDK_INT >= 17) {
                            configuration.setLocale(locale);
                        } else {
                            configuration.locale = locale;
                        }
                        resources.updateConfiguration(configuration, displayMetrics);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                    ALog.e("LanguageManager", "set oa language throw = " + th);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ALog.e("LanguageManager", "set oa language error:" + e.toString());
            }
            if (str.equals(GlobalConfig.getInstance().getLanguage())) {
                ALog.e("LanguageManager", "same language, ignore");
                return;
            }
            try {
                IoTAPIClientImpl.getInstance().setLanguage(makeLanguageString(locale));
            } catch (Exception e2) {
                e2.printStackTrace();
                ALog.e("LanguageManager", "set apiclient language error:" + e2.toString());
            } catch (Throwable th2) {
                th2.printStackTrace();
                ALog.e("LanguageManager", "set apiclient language throe:" + th2);
            }
            if (SDKManager.isRNAvailable()) {
                BoneConfig.set(EnvConfigure.KEY_LANGUAGE, makeLanguageString(locale));
            }
            ALog.e("LanguageManager", "set language Push bindUser");
            try {
                PushManagerHelper.getInstance().bindUserSafely();
            } catch (Throwable th3) {
                th3.printStackTrace();
                ALog.e("LanguageManager", "PushManagerHelper.getInstance().bindUser throw:" + th3);
            }
            GlobalConfig.getInstance().setLanguage(str);
        } catch (Exception e3) {
            e3.printStackTrace();
            ALog.e("LanguageManager", "set language error:" + e3.toString());
        }
    }

    public static void handleLanguageChanged() {
        a();
    }

    public static boolean needSwitchLanguage() {
        return f4682a;
    }

    public static Context replaceLanguage(Context context) {
        if (!f4682a || Build.VERSION.SDK_INT < 24) {
            return context;
        }
        final Configuration configuration = context.getResources().getConfiguration();
        String[] strArrLoadLanguageInfo = LanguageHelper.LoadLanguageInfo(loadAppLanguage());
        Locale locale = AApplication.getInstance().getResources().getConfiguration().locale;
        try {
            locale = new Locale(strArrLoadLanguageInfo[0], strArrLoadLanguageInfo[1]);
        } catch (Exception unused) {
        }
        configuration.setLocales(new LocaleList(locale));
        return new ContextThemeWrapper(context.createConfigurationContext(configuration), R.style.Theme_AppCompat_Empty) { // from class: com.aliyun.iot.aep.sdk.framework.language.LanguageManager.1
            @Override // androidx.appcompat.view.ContextThemeWrapper
            public void applyOverrideConfiguration(Configuration configuration2) {
                if (configuration2 != null) {
                    configuration2.setTo(configuration);
                }
                super.applyOverrideConfiguration(configuration2);
            }
        };
    }

    public static void updateApplicationLanguage(Resources resources) {
        if (f4682a) {
            a(resources);
        }
    }

    public static String loadAppLanguage() {
        String language = IoTSmart.getLanguage();
        ALog.d("LanguageManager", "load language success, language:" + language);
        return language;
    }

    private static void a() {
        if (f4682a) {
            a(AApplication.getInstance().getResources());
        }
    }

    private static void a(Resources resources) {
        if (f4682a && resources != null) {
            String strLoadAppLanguage = loadAppLanguage();
            if (!TextUtils.isEmpty(strLoadAppLanguage)) {
                try {
                    String[] strArrLoadLanguageInfo = LanguageHelper.LoadLanguageInfo(strLoadAppLanguage);
                    a(resources, new Locale(strArrLoadLanguageInfo[0], strArrLoadLanguageInfo[1]));
                    return;
                } catch (Exception unused) {
                    ALog.e("LanguageManager", "use sharedPreference language failed, will use default language");
                }
            }
            String language = resources.getConfiguration().locale.getLanguage();
            if ("zh".equalsIgnoreCase(language)) {
                a(resources, Locale.SIMPLIFIED_CHINESE);
                return;
            }
            if ("fr".equalsIgnoreCase(language)) {
                a(resources, Locale.FRANCE);
                return;
            }
            if ("de".equalsIgnoreCase(language)) {
                a(resources, Locale.GERMANY);
                return;
            }
            if ("en".equalsIgnoreCase(language)) {
                a(resources, Locale.US);
                return;
            }
            if ("ja".equalsIgnoreCase(language)) {
                a(resources, Locale.JAPAN);
                return;
            }
            if ("ko".equalsIgnoreCase(language)) {
                a(resources, Locale.KOREA);
                return;
            }
            if ("es".equalsIgnoreCase(language)) {
                a(resources, new Locale("es", "ES"));
                return;
            }
            if ("ru".equalsIgnoreCase(language)) {
                a(resources, new Locale("ru", "RU"));
                return;
            }
            if ("hi".equalsIgnoreCase(language)) {
                a(resources, new Locale("hi", "IN"));
                return;
            }
            if ("it".equalsIgnoreCase(language)) {
                a(resources, new Locale("it", "IT"));
                return;
            }
            if ("pt".equalsIgnoreCase(language)) {
                a(resources, new Locale("pt", "PT"));
                return;
            }
            if ("nl".equalsIgnoreCase(language)) {
                a(resources, new Locale("nl", "NL"));
            } else if ("pl".equalsIgnoreCase(language)) {
                a(resources, new Locale("pl", "PL"));
            } else {
                a(resources, Locale.US);
            }
        }
    }

    private static void a(Resources resources, Locale locale) {
        if (resources == null || locale == null) {
            return;
        }
        Configuration configuration = resources.getConfiguration();
        if (configuration == null) {
            ALog.e("LanguageManager", "configuration is null");
            return;
        }
        if (configuration.locale.equals(locale)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        ALog.d("LanguageManager", "current language = " + LanguageHelper.MakeLanguageString(locale));
    }
}

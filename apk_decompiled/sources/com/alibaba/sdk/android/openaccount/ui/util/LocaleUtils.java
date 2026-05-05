package com.alibaba.sdk.android.openaccount.ui.util;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;

/* JADX INFO: loaded from: classes.dex */
public class LocaleUtils {
    private static final String DEFAULT_LOCALE = "zh_CN";

    public static String getCurrentLocale() {
        return TextUtils.isEmpty(OpenAccountConfigs.clientLocal) ? "zh_CN" : OpenAccountConfigs.clientLocal;
    }

    public static boolean isENLocale(String str) {
        return (str == null || str.toLowerCase().indexOf("en") == -1) ? false : true;
    }

    public static boolean isZHLocale(String str) {
        return (str == null || str.toLowerCase().indexOf("zh") == -1) ? false : true;
    }

    public static boolean isUseTraditionChinese(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return (lowerCase.indexOf("tw") == -1 && lowerCase.indexOf("hk") == -1) ? false : true;
    }
}

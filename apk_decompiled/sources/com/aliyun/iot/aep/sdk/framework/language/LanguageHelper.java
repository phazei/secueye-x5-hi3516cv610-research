package com.aliyun.iot.aep.sdk.framework.language;

import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.xiaomi.mipush.sdk.Constants;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class LanguageHelper {
    public static String MakeLanguageString(Locale locale) {
        return locale.getLanguage() + Constants.ACCEPT_TIME_SEPARATOR_SERVER + locale.getCountry();
    }

    public static String[] LoadLanguageInfo(String str) {
        String[] strArrSplit = str.split(Constants.ACCEPT_TIME_SEPARATOR_SERVER);
        return strArrSplit.length == 1 ? str.split(OpenAccountUIConstants.UNDER_LINE) : strArrSplit;
    }
}

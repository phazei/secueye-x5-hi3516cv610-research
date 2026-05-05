package com.aliyun.iot.aep.oa;

import android.content.res.Configuration;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class OALanguageHelper {
    public static void setLanguageCode(Locale locale) throws Exception {
        if (locale != null && locale.getLanguage() != null && locale.getCountry() != null) {
            Configuration configuration = OpenAccountSDK.getAndroidContext().getResources().getConfiguration();
            configuration.locale = locale;
            OpenAccountSDK.getAndroidContext().getResources().updateConfiguration(configuration, OpenAccountSDK.getAndroidContext().getResources().getDisplayMetrics());
            ConfigManager.getInstance().setLanguageCode(locale.getLanguage() + OpenAccountUIConstants.UNDER_LINE + locale.getCountry());
            return;
        }
        throw new Exception("local has error");
    }
}
